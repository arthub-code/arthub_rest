package br.com.arthub.ah_rest_useraccount.api.v1.service.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailAlreadyInUseException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.PasswordIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameAlreadyInUseException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRequestRepository;

@Service
public class UserAccountUtilsService {
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private UserAccountRequestRepository reqRepository;
	
	/**
	 * @param username
	 * 
	 * <p>Checa se o username informado já não está sendo utilizado.</p>
	 * <p>Caso esteja, o método lançará uma exception unchecked
	 * {@link br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameAlreadyInUseException}</p>
	 * <p>e se não estiver, o método não retornará nada.</p>
	 * */
	public void checkIfTheUsernameIsInUse(String username) {
		String toValidate = username.trim().toLowerCase();
		if(!toValidate.startsWith("@"))
			toValidate = "@" + toValidate;
		if(repository.usernameInUse(username) || reqRepository.usernameInUse(username))
			throw new UsernameAlreadyInUseException();
	}

	public void validateAccountBeforeRegister(CreateAnAccount createDto, PasswordEncoder encoder) {
		if(!checkIfTheUsernameIsValid(createDto.getUsername()))
			throw new UsernameIsInvalidException();
		
		checkIfTheUsernameIsInUse(createDto.getUsername());
		createDto.setUsername(formatUsername(createDto.getUsername()));
		
		if(!checkIfThePasswordIsValid(createDto.getPassword()))
			throw new PasswordIsInvalidException();
		createDto.setPassword(encoder.encode(createDto.getPassword()));

		checkIfTheEmailIsInUse(createDto.getEmail());

		if(!checkIfTheEmailIsValid(createDto.getEmail()))
			throw new EmailInvalidException();
		
		if(createDto.getFullName().isBlank()) 
			throw new RuntimeException("The social name fiedld is required.");
		else if(createDto.getFullName().length() < 10)
			throw new RuntimeException("The social name must contain at least 10 characters");
		
		if(createDto.getDateOfBirth() == null)
			throw new RuntimeException("The date of birth fiedld is required.");
		else if(Period.between(createDto.getDateOfBirth(), LocalDate.now()).getYears() < 15)
			throw new RuntimeException("You must be at least 15 years old to create your account.");
	}
	
	/**
	 * @param email
	 * 
	 * <p>Checa se o email informado já não está sendo utilizado.</p>
	 * <p>Caso esteja, o método lançará uma exception unchecked
	 * {@link br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailAlreadyInUseException}</p>
	 * <p>e se não estiver, o método não retornará nada.</p>
	 * */
	public void checkIfTheEmailIsInUse(String email) {
		if(repository.emailInUse(email) || reqRepository.emailInUse(email))
			throw new EmailAlreadyInUseException();
	}
	
	/**
	 * @param email
	 * 
	 * <p>Checa se o email informado já não está sendo utilizado.</p>
	 * <p>Caso esteja, o método lançará uma exception unchecked
	 * {@link br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailAlreadyInUseException}</p>
	 * <p>e se não estiver, o método não retornará <code>false</code>.</p>
	 * */
	public boolean checkIfTheEmailIsInUseBool(String email) {
		return (repository.emailInUse(email) || reqRepository.emailInUse(email));
	}

	/**
	 * @param email
	 * 
	 * <p>Checa se o email é de fato um email.</p>
	 * @return <code><strong>true</strong></code> caso seja um email válido e <code><strong>false</strong></code> caso não seja.
	 */
	public boolean checkIfTheEmailIsValid(String email) {
		if (email == null) {
            return false;
        }
        String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
	
	/**
	 * @param username
	 * 
	 * <p>Checa se o username é válido.</p>
	 * <p>O username pode conter os seguintes caracteres:</p>
	 * <ul>
	 * 	<li><code>Letras</code></li>
	 * 	<li><code>Números</code></li>
	 * 	<li><code>'-' '_' '.'</code></li>
	 * </ul>
	 * 
	 * <p>De resto, nenhum outro caractere especial é permitido.</p>
	 * 
	 * @return <code><strong>true</strong></code> se o username for válido e <code><strong>false</strong></code> se não for.
	 * */
	public boolean checkIfTheUsernameIsValid(String username) {
		String toFormat = username.trim().toLowerCase();
		if(!toFormat.startsWith("@"))
			toFormat = "@" + toFormat;
		String regex = "^@[a-z0-9._-]+$";
		return toFormat.matches(regex);
	}
	
	/**
	 * @param username
	 * 
	 * <p>Formata um username para ser salvo.</p>
	 * 
	 * @return o username formatado.
	 * */
	public String formatUsername(String username) {
		String toFormat = username.trim().toLowerCase();
		if(!toFormat.startsWith("@"))
			toFormat = "@" + toFormat;
		return toFormat;
	}
	
	/**
	 * @param password
	 * 
	 * <p>Verifica se a senha informada é válida.</p>
	 * <p>A senha deve conter no mínimo:</p>
	 * <ul>
	 * 	<li>8 caracteres</li>
	 * 	<li>1 caracter especial</li>
	 * 	<li>1 letra maiúscula</li>
	 * </ul>
	 * 
	 * @return <code><strong>true</strong></code> se a senha for válida e <code><strong>false</strong></code> se não for.
	 * */
	public boolean checkIfThePasswordIsValid(String password) {
		if (password.length() < 8)
	        return false;
		
		String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";
		return password.matches(regex);
	}

	public String createConfirmationEndpoint(String token) {
		return "/useraccount/v1/public/confirmAccount?token=" + token;
	}
	
	public String createPasswordChangeLink(String token) {
		return "/passwordChange?token=" + token;
	}
} 
