package br.com.arthub.ah_rest_useraccount.api.v1.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.service.UserAccountService;

@RestController
@RequestMapping("/v1")
public class UserAccountController extends ControllerModel {
	@Autowired
	private UserAccountService accountService;
	
	@Value("${arthub.ms.secrets.user-id-by-token}")
	private String secretCallUserIdByToken;
	
	@GetMapping("/accountExists")
	public ResponseEntity<ApiResponse> doCheckIfAccountExists(@RequestParam(name = "accountId", required = true) UUID accountId) {
		setOkResponse(() -> accountService.doCheckIfAccountExists(accountId));
		return response();
	}
	
	@GetMapping("/getIdByToken")
	public ResponseEntity<ApiResponse> getUserAccountIdByToken(@RequestParam(name = "tokenJwt", required = true) String token,
			@RequestParam(name = "secret", required = true) String secret) {
		if(!secretCallUserIdByToken.equals(secret))
			return ResponseEntity.status(UNAUTHORIZED).build();
		
		setOkResponse(() -> accountService.getUserAccountIdByToken(token));
		return response();
	}
	
	@GetMapping("/getArtistIdByToken")
	public ResponseEntity<ApiResponse> getArtistIdByToken(@RequestParam(name = "tokenJwt", required = true) String token,
			@RequestParam(name = "secret", required = true) String secret) {
		if(!secretCallUserIdByToken.equals(secret))
			return ResponseEntity.status(UNAUTHORIZED).build();
		
		setOkResponse(() -> accountService.getArtistIdByToken(token));
		return response();
	}
}
