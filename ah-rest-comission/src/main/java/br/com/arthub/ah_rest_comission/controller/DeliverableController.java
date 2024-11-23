package br.com.arthub.ah_rest_comission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_comission.dto.ApiResponse;

@RestController
@RequestMapping("/v1/deliverable")
public class DeliverableController extends ControllerModel {
	/* Controle de Entregas; */
	
	@PostMapping("/send")
	public ResponseEntity<ApiResponse> doSendDeliverable() {
		return response();
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> doUpdateDeliverable() {
		return response();
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse> doDeleteDeliverable() {
		return response();
	}
	
	@GetMapping("/list")
	public ResponseEntity<ApiResponse> doListDeliverable() {
		return response();
	}
	
	@GetMapping("/get-file")
	public ResponseEntity<byte[]> getDeliverableile() {
		return null;
	}
}
