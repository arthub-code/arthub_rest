package br.com.arthub.ah_rest_art.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_art.dto.ApiResponse;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.service.ArtService;

@RestController
@RequestMapping("/v1")
public class ArtController extends ControllerModel {
	@Autowired
	private ArtService artService;
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> doCreateAnArt(@RequestBody ArtPayload payload) {
		setCreatedResponse(() -> artService.doCreateAnArt(payload));
		return response();
	};
}
