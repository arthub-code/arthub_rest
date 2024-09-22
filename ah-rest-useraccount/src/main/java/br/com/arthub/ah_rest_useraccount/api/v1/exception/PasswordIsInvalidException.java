package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import lombok.Getter;

public class PasswordIsInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public PasswordIsInvalidException() {
		/* Mensagem de erro padrão */
		super("The password must contain at least 8 characters, one capital letter and one special character.");
		this.message = "The password must contain at least 8 characters, one capital letter and one special character.";
	}
	
	public PasswordIsInvalidException(String message) {
		super(message);
		this.message = message;
	}
} 
