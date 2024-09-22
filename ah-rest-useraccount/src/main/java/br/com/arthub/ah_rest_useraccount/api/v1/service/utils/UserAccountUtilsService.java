package br.com.arthub.ah_rest_useraccount.api.v1.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameAlreadyInUseException;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;

@Service
public class UserAccountUtilsService {
	@Autowired
	private UserAccountRepository repository;
	
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
		if(repository.usernameInUse(username))
			throw new UsernameAlreadyInUseException();
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
} 
