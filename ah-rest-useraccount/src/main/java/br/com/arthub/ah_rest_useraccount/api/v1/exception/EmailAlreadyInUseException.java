package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import lombok.Getter;

public class EmailAlreadyInUseException extends RuntimeException{
    private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
    public EmailAlreadyInUseException() {
		/* Mensagem de erro padrão */
		super("This email is already in use.");
		this.message = "This email is already in use.";
	}
	
	public EmailAlreadyInUseException(String message) {
		super(message);
		this.message = message;
	}
}
