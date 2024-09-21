package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import lombok.Getter;

public class UsernameIsInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public UsernameIsInvalidException() {
		/* Mensagem de erro padrão */
		super("The username is invalid and can only contain the following special characters - _ .");
		this.message = "The username is invalid and can only contain the following special characters - _ .";
	}
	
	public UsernameIsInvalidException(String message) {
		super(message);
		this.message = message;
	}
}
