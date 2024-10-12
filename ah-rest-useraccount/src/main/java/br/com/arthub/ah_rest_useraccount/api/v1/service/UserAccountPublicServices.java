package br.com.arthub.ah_rest_useraccount.api.v1.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arthub.ah_rest_useraccount.api.v1.config.RabbitMQConfig;
import br.com.arthub.ah_rest_useraccount.api.v1.constants.EmailOrder;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.Authenticated;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.ChangePassword;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.EmailRequest;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.LoginResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.UserCredentials;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountRequestEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.PasswordIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.TokenExpiredException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UnauthorizatedException;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.service.utils.UserAccountUtilsService;
import br.com.arthub.ah_rest_useraccount.api.v1.utils.JwtUtils;

@Service
public class UserAccountPublicServices {
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private UserAccountUtilsService utils;
	@Autowired
	private UserAccountRequestService accountReqService;
	@Autowired
	private UserAccountProfileService profileService;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private PasswordEncoder encoder;
	
	/**
	 * @param createDto
	 * 
	 * <p>Realiza uma solicitação de registro de conta de usuário no sistema do <a href='https://www.arthub.com.br'>ArtHub</a>.</p>
	 * <br>
	 * <p>Seguintes validações:</p>
	 * <ul>
	 * 	<li>Validação de username</li>
	 * 	<li>Validação de senha</li>
	 * </ul>
	 * 
	 * <p>Seguintes serviços chamados:</p>
	 * <ul>
	 * 	<li>Serviço de confirmação de email.</li>
	 * <ul>
	 * */
	public String doRequestAccountCreation(CreateAnAccount createDto) throws Exception {
		accountReqService.clearExpiredDatas();
		utils.validateAccountBeforeRegister(createDto, encoder);
		
		String token = jwtUtils.generateConfirmationEmailToken(createDto.getEmail());
		String accountConfirmationEndpoint = utils.createConfirmationEndpoint(token);
				
		// salva na tabela de requisição de contas
		accountReqService.saveUserAccountData(createDto, token);

		EmailRequest emailRequest = new EmailRequest(
			"Account requested successfully! Waiting for email confirmation \"" + createDto.getEmail()  +"\".",
			token,
			accountConfirmationEndpoint,
			createDto.getEmail(),
			"Confirmação de email",
			EmailOrder.CONFIRMATION,
			createDto.getSocialName()
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(emailRequest);

		// publicar na fila de email uma requisição de conta;
		rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, json);
		return "Account requested successfully! Waiting for email confirmation \"" + createDto.getEmail()  +"\".";
	}

	public String doConfirmAccount(String token) {
		// Extrai o email do token
		String email = jwtUtils.extractUsername(token);
		UserAccountRequestEntity req = accountReqService.findByEmail(email);

		if(req == null)
			throw new RuntimeException("This account has already been registered.");
		
		if(jwtUtils.isTokenExpired(token))
			throw new RuntimeException("Invalid or expired token.");
		
		if (!jwtUtils.isClaimValid(token, JwtUtils.CL_CONFIRM_EMAIL))
	        throw new RuntimeException("Invalid token purpose.");

		UserAccountEntity accountEntity = new UserAccountEntity(req);
		try {
			UserAccountEntity registred = this.repository.saveAndFlush(accountEntity);
			if(registred == null)
				throw new RuntimeException("An unexpected error occurred while trying to register the account with email \"" + email + "\". Please contact support for more information.");
			profileService.doRegisterProfile(req, registred);
		} catch(Exception e) {
			throw new RuntimeException("An unexpected error occurred while trying to register the account with email \"" + email + "\". Please contact support for more information.");
		}
		
		this.accountReqService.delete(req);
		return "Email confirmed successfully! \"" + accountEntity.getAccountUsername() + "\"'s account has been registered.";
	}
	
	public LoginResponse doAuth(UserCredentials credentials) {
		 Optional<UserAccountEntity> optionalUser = repository.findByEmail(credentials.getEmail());
		 if(!optionalUser.isEmpty()) {
			 UserAccountEntity user = optionalUser.get();
			 if (encoder.matches(credentials.getPassword(), user.getAccountPassword())) {
	            String token = jwtUtils.generateAuthToken(user.getAccountEmail());
	            return new Authenticated(
	            			user.getUserAccountId(),
	            			user.getAccountUsername(),
	            			token,
	            			true,
	            			"User authenticated!"
	            		);
	        } else
	            throw new UnauthorizatedException(new LoginResponse(false , "Invalid data."));
		 } else 
	            throw new UnauthorizatedException(new LoginResponse(false, "Usuário inexistente."));
	}
	
	public LoginResponse doValidateAuthToken(String authToken) {
		if (authToken == null || authToken.trim().isEmpty() || authToken.split("\\.").length != 3) {
		    throw new UnauthorizatedException(new LoginResponse(false, "Invalid token format."));
		}
		
		String token = authToken;
		String email = jwtUtils.extractUsername(token);
		Optional<UserAccountEntity> optionalUser = repository.findByEmail(email);
		if(optionalUser.isEmpty())
			throw new UnauthorizatedException(new LoginResponse(false , "Invalid user."));
		if(jwtUtils.isTokenExpired(token))
			throw new UnauthorizatedException(new LoginResponse(false , "The Token has expired on \"" + jwtUtils.getExpiresAt(token) + "\"."));
			
		return new LoginResponse(true, "User authenticated!");
	}
	
	
	public String requestChangePasswordService(String email) throws Exception {
		if(!utils.checkIfTheEmailIsValid(email))
			throw new EmailInvalidException();
		
		if(!utils.checkIfTheEmailIsInUseBool(email))
			throw new RuntimeException("The email provided is not registered with Arthub.");
		
		UserAccountEntity account = repository.findByEmail(email).get();
		String token = jwtUtils.generatePasswordResetToken(email);
		
		EmailRequest emailRequest = new EmailRequest(
				"Password change link sent successfully to email \"" + email +"\".",
				null,
				utils.createPasswordChangeLink(token),
				email,
				"Troca de Senha",
				EmailOrder.PASSWORD_CHANGE,
				account.getAccountSocialName()
			);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(emailRequest);

		// publicar na fila de email uma requisição de conta;
		rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, json);
		return "Password change link sent successfully to email \"" + email +"\".";
	}
	
	public String doChangePassword(String token, ChangePassword payload) {
		UserAccountEntity account = validateToken(token, JwtUtils.CL_PASSWORD_RESET);
		if(account == null)
			throw new RuntimeException("Unable to retrieve user from sent token.");
		
		if(utils.checkIfThePasswordIsValid(payload.getNewPassword()))
			throw new PasswordIsInvalidException();
		
		account.setAccountPassword(encoder.encode(payload.getNewPassword()));
		repository.saveAndFlush(account);
		return "Account password changed successfully.";
	}
	
	public String validatePasswordChangeToken(String token) {
		validateToken(token, JwtUtils.CL_PASSWORD_RESET);
		return "Valid token.";
	}
	
	private UserAccountEntity validateToken(String token, String claim) {
	    if (jwtUtils.isTokenExpired(token))
	        throw new TokenExpiredException(jwtUtils.getExpiresAt(token));

	    if (!jwtUtils.isClaimValid(token, claim))
	        throw new RuntimeException("Invalid token purpose.");

	    String email = jwtUtils.extractUsername(token);
	    Optional<UserAccountEntity> opAccount = repository.findByEmail(email);
	    if (opAccount.isEmpty())
	        throw new RuntimeException("User account not found.");

	    return opAccount.get();
	}	
}
