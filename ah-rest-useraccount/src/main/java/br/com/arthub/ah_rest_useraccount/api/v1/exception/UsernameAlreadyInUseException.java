package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import lombok.Getter;

public class UsernameAlreadyInUseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public UsernameAlreadyInUseException() {
		/* Mensagem de erro padrão */
		super("This username is already in use.");
		this.message = "This username is already in use.";
	}
 	
	public UsernameAlreadyInUseException(String message) {
		super(message);
		this.message = message;
	}
} 
