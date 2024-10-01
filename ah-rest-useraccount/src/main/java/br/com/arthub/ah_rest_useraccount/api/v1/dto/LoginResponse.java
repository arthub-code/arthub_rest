package br.com.arthub.ah_rest_useraccount.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	protected boolean authenticated;
	protected String message;
}
