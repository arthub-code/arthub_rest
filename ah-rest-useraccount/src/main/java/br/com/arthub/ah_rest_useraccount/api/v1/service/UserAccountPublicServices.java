package br.com.arthub.ah_rest_useraccount.api.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.PasswordIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.service.utils.UserAccountUtilsService;

@Service
public class UserAccountPublicServices {
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private UserAccountUtilsService utils;
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
	public String doRequestAccountCreation(CreateAnAccount createDto) {
		if(!utils.checkIfTheUsernameIsValid(createDto.getUsername()))
			throw new UsernameIsInvalidException();
		
		utils.checkIfTheUsernameIsInUse(createDto.getUsername());
		createDto.setUsername(utils.formatUsername(createDto.getUsername()));
		
		if(!utils.checkIfThePasswordIsValid(createDto.getPassword()))
			throw new PasswordIsInvalidException();
		createDto.setPassword(encoder.encode(createDto.getPassword()));
		
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
}
