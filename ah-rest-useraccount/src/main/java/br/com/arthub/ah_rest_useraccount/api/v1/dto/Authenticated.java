package br.com.arthub.ah_rest_useraccount.api.v1.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authenticated extends LoginResponse {
	private String username;
	private String token;
	
	public Authenticated(UUID accountId, String username, String token, boolean authenticated, String message) {
		super(authenticated, message);
		this.username = username;
		this.token = token;
	}
}
