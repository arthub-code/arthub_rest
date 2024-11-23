package br.com.arthub.ah_rest_comission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_comission.dto.ApiResponse;
import br.com.arthub.ah_rest_comission.dto.ServicePayload;
import br.com.arthub.ah_rest_comission.service.ServiceService;

@RestController
@RequestMapping("/v1/service")
public class ServiceController extends ControllerModel {
	@Autowired
	private ServiceService service;
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> doCreateService(@RequestBody ServicePayload payload, 
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setCreatedResponse(() -> service.doCreate(authToken, payload));
		return response();
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> doUpdateService() {
		return response();
	}
	
	@GetMapping("/list")
	public ResponseEntity<ApiResponse> getAllServices() {
		return response();
	}
	
	@GetMapping("/details/{id}")
	public ResponseEntity<ApiResponse> getService() {
		return response();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> doDeleteService() {
		return response();
	}
}
