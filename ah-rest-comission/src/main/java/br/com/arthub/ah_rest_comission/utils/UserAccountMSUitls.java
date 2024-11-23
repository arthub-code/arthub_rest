package br.com.arthub.ah_rest_comission.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.arthub.ah_rest_comission.dto.ApiResponse;
import br.com.arthub.ah_rest_comission.feign.client.UserAccountFeignClient;

@Component
public class UserAccountMSUitls {
	@Autowired
	private UserAccountFeignClient accountFeignClient;
	
	@Value("${arthub.ms.secrets.user-id-by-token}")
	private String secretCallUserIdByToken;
	
	public UUID getArtistIdByToken(String tokenJwt) {
		UUID accountId = null;
		
		if(tokenJwt.isBlank() || !tokenJwt.startsWith("Bearer "))
			throw new RuntimeException("The jwt token is required.");
		
		String token = tokenJwt.substring(7);
		try {
			ResponseEntity<ApiResponse> response =  accountFeignClient.getArtistIdByToken(token, secretCallUserIdByToken);
			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
	            throw new RuntimeException("Artist account not found or the user account service could not be called.");
	        }
			
			accountId = UUID.fromString((String) response.getBody().getData());
		} catch (Exception e) {
			throw new RuntimeException("Artist account not found.");
		}
		
		return accountId;
	}
}
