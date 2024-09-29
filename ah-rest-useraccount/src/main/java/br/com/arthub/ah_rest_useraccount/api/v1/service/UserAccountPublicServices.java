package br.com.arthub.ah_rest_useraccount.api.v1.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arthub.ah_rest_useraccount.api.v1.config.RabbitMQConfig;
import br.com.arthub.ah_rest_useraccount.api.v1.constants.EmailOrder;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.EmailRequest;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountRequestEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.PasswordIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.service.utils.UserAccountUtilsService;
import br.com.arthub.ah_rest_useraccount.api.v1.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAccountPublicServices {
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private UserAccountUtilsService utils;
	@Autowired
	private UserAccountRequestService accountReqService;
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

		if(!utils.checkIfTheUsernameIsValid(createDto.getUsername()))
			throw new UsernameIsInvalidException();
		
		utils.checkIfTheUsernameIsInUse(createDto.getUsername());
		createDto.setUsername(utils.formatUsername(createDto.getUsername()));
		
		if(!utils.checkIfThePasswordIsValid(createDto.getPassword()))
			throw new PasswordIsInvalidException();
		createDto.setPassword(encoder.encode(createDto.getPassword()));

		utils.checkIfTheEmailIsInUse(createDto.getEmail());

		if(!utils.checkIfTheEmailIsValid(createDto.getEmail()))
			throw new EmailInvalidException();

		String token = jwtUtils.generateTokenToAccountConfirmation(createDto.getEmail());
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
	
	/**
	 * @param createDto
	 * 
	 * <p>Realiza um registro de conta de usuário na base de dados do <a href='https://www.arthub.com.br'>ArtHub</a>.</p>
	 * */
	public void doCreateUserAccount(CreateAnAccount createDto) {
		UserAccountEntity entity = new UserAccountEntity(createDto);
		entity.setAccountUsername(createDto.getUsername());
		entity.setAccountPassword(createDto.getPassword());
		repository.saveAndFlush(entity);
	}

	public String doConfirmAccount(String token) {
		// Extrai o email do token
		String email = jwtUtils.extractUsername(token);
		UserAccountRequestEntity req = accountReqService.findByEmail(email);

		log.info("email: {}", email);
		log.info("req account: {}", req);

		if(jwtUtils.isTokenExpired(token))
			throw new RuntimeException("Invalid or expired token.");

		UserAccountEntity accountEntity = new UserAccountEntity(req);
		try {
			this.repository.saveAndFlush(accountEntity);
		} catch(Exception e) {
			throw new RuntimeException("An unexpected error occurred while trying to register the account with email \"" + email + "\". Please contact support for more information.");
		}
		
		this.accountReqService.delete(req);
		return "Email confirmed successfully! \"" + accountEntity.getAccountUsername() + "\"'s account has been registered.";
	}
}
