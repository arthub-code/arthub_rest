package br.com.arthub.ah_rest_useraccount.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.service.UserAccountPublicServices;

@RestController
@RequestMapping("/public")
public class UserAccountPublicController extends ControllerModel {
	@Autowired
	private UserAccountPublicServices publicService;
	
	@PostMapping("/requestCreationUserAccount")
	public ResponseEntity<ApiResponse> doRequestCreationUserAccount(@RequestBody CreateAnAccount createDto) {
		setOkResponse(() -> publicService.doRequestAccountCreation(createDto));
		return response();
	}
}
