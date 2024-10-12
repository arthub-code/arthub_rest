package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import java.util.Date;

import lombok.Getter;

public class TokenExpiredException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public TokenExpiredException(Date expiredAt) {
		/* Mensagem de erro padrão */
		super("The Token has expired on \"" + expiredAt + "\".");
		this.message = "The Token has expired on \"" + expiredAt + "\".";
	}
	
}
