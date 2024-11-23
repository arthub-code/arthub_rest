package br.com.arthub.ah_rest_comission.exception;

import lombok.Getter;

public class NullObjectException extends Exception {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public NullObjectException(String message) {
		super(message);
		this.message = message;
	}
}
