package br.com.arthub.ah_rest_useraccount.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.ChangePassword;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.UserCredentials;
import br.com.arthub.ah_rest_useraccount.api.v1.service.UserAccountPublicServices;

@RestController
@RequestMapping("/v1/public")
public class UserAccountPublicController extends ControllerModel {
	@Autowired
	private UserAccountPublicServices publicService;
	
	@PostMapping("/requestCreationUserAccount")
	public ResponseEntity<ApiResponse> doRequestCreationUserAccount(@RequestBody CreateAnAccount createDto) {
		setOkResponse(() -> publicService.doRequestAccountCreation(createDto));
		return response();
	}

	@GetMapping("/confirmAccount")
	public ResponseEntity<ApiResponse> doConfirmAccount(@RequestParam(name = "token", required = true) String token) {
		setOkResponse(() -> publicService.doConfirmAccount(token));
		return response();
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> doLogin(@RequestBody UserCredentials credentials) {
		setOkResponse(() -> publicService.doAuth(credentials));
		return response();
	}
	
	@GetMapping("/validateToken")
	public ResponseEntity<ApiResponse> doValidateToken(@RequestParam(name = "token", required = true) String token) {
		setOkResponse(() -> publicService.doValidateAuthToken(token));
		return response();
	}
	
	@PatchMapping("/changePassword/request")
	public ResponseEntity<ApiResponse> requestChangePasswordService(@RequestParam(name = "accountEmail", required = true) String email) {
		setOkResponse(() -> publicService.requestChangePasswordService(email));
		return response();
	}
	
	@PostMapping("/changePassword/change")
	public ResponseEntity<ApiResponse> doChangePassword(@RequestParam(name = "token", required = true) String token,
			@RequestBody ChangePassword payload) {
		setOkResponse(() -> publicService.doChangePassword(token, payload));
		return response();
	}
	
	@GetMapping("/changePassword/validateToken")
	public ResponseEntity<ApiResponse> getValidateTokenToPasswordChangeService(@RequestParam(name = "token", required = true) String token) {
		setOkResponse(() -> publicService.validatePasswordChangeToken(token));
		return response();
	}
}
