package br.com.arthub.ah_rest_art.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_art.dto.ApiResponse;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.dto.UpdateArtImageRefPayload;
import br.com.arthub.ah_rest_art.dto.UpdateArtPayload;
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
	
	@GetMapping("/arts")
	public ResponseEntity<ApiResponse> getAllUserArts(@RequestParam(name = "accountId", required = true) UUID userAccountId) {
		setOkResponse(() -> artService.getAllUserArts(userAccountId));
		return response();
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> doUpdateArtData(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "accountId", required = true) UUID accountId,
			@RequestBody UpdateArtPayload updatePayload) {
		setOkResponse(() -> artService.doUpdateArt(artId, accountId, updatePayload));
		return response();
	}
	
	@DeleteMapping("/fullDelete")
	public ResponseEntity<?> doFullDeleteArt(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "accountId", required = true) UUID accountId) {
		try {
			artService.doFullDeleteArt(artId, accountId);
		} catch(Exception e ) {
			return ResponseEntity.status(BAD_REQUEST).body(
						new ApiResponse(BAD_REQUEST, true, e.getMessage())
					);
		}
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/imgRef/update")
	public ResponseEntity<ApiResponse> doUpdateAllArtImageRefs(@RequestBody UpdateArtImageRefPayload uploadArtImgRefPayload,
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "accountId", required = true) UUID accountId) {
		setOkResponse(() -> artService.doUpdateAllArtImageRefs(artId, accountId, uploadArtImgRefPayload));
		return response();
	}
}
