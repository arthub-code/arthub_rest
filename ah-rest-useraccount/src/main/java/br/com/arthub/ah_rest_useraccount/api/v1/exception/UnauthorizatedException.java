package br.com.arthub.ah_rest_useraccount.api.v1.exception;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.LoginResponse;
import lombok.Getter;

public class UnauthorizatedException extends RuntimeException {
	@Getter
	private LoginResponse response;

	public UnauthorizatedException() {
		/** Mensagem de erro padrão. */
		super("Invalid data.");
		this.response = new LoginResponse(false, "Invalid data.");
	}
	
	public UnauthorizatedException(LoginResponse response) {
		super(response.getMessage());
		this.response = response;
	}
}
