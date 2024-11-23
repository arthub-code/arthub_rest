package br.com.arthub.ah_rest_comission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_comission.dto.ApiResponse;

@RestController
@RequestMapping("/v1")
public class ComissionController extends ControllerModel {
	
	/* Endpoints referentes a criação de uma comissão no sistema. */
	/* Primeiro o cliente requisita uma comissão que cairá na caixa de comissões do artista. */
	/* Segundo o artista recebe a solicitação e sua caixa e decide se irá aceitar a comissão. */
	
	@PostMapping("/requestCreationComission")
	public ResponseEntity<ApiResponse> doRequestComission() {
		return response();
	}
	
	@PatchMapping("/confirmComission")
	public ResponseEntity<ApiResponse> doConfirmComission() {
		return response();
	}
	
	@PatchMapping("/denyComission")
	public ResponseEntity<ApiResponse> doDenyComission() {
		return response();
	}
	
	
	/* Endpoints referente a página de controle da comissões: */
	/* Infos da comissão; */
	/* Ações de controle da comissão; */
	
	@GetMapping("/info")
	public ResponseEntity<ApiResponse> getComissionInfo() {
		return response();
	}
	
	@PatchMapping("/cancelComission")
	public ResponseEntity<ApiResponse> doCancelComission() {
		return response();
	}
	
	@PatchMapping("/updateComissionData")
	public ResponseEntity<ApiResponse> doUpdateComissionData() {
		return response();
	}
	
	@PatchMapping("/doFinalizeComission")
	public ResponseEntity<ApiResponse> doFinalizeComission() {
		return response();
	}
}
