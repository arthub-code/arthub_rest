package br.com.arthub.ah_rest_comission.exception;

import lombok.Getter;

public class NullAttributeExcpetion extends NullObjectException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	public NullAttributeExcpetion(String message) {
		super(message);
		this.message = message;
	}
}
