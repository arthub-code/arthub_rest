package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import lombok.Getter;

public class EmailInvalidException extends RuntimeException{
private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public EmailInvalidException() {
		/* Mensagem de erro padrão */
		super("The email is invalid.");
		this.message = "The email is invalid.";
	}
	
	public EmailInvalidException(String message) {
		super(message);
		this.message = message;
	}
}
