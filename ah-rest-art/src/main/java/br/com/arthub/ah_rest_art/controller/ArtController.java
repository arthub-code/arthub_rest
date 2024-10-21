package br.com.arthub.ah_rest_art.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<ApiResponse> doCreateAnArt(@RequestBody ArtPayload payload,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setCreatedResponse(() -> artService.doCreateAnArt(payload, authToken));
		return response();
	};
	
	@GetMapping("/arts")
	public ResponseEntity<ApiResponse> getAllUserArts(@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> artService.getAllUserArts(authToken));
		return response();
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> doUpdateArtData(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken,
			@RequestBody UpdateArtPayload updatePayload) {
		setOkResponse(() -> artService.doUpdateArt(artId, authToken, updatePayload));
		return response();
	}
	
	@DeleteMapping("/fullDelete")
	public ResponseEntity<?> doFullDeleteArt(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		try {
			artService.doFullDeleteArt(artId, authToken);
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
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> artService.doUpdateAllArtImageRefs(artId, authToken, uploadArtImgRefPayload));
		return response();
	}
	
	@PatchMapping("/changeStatus")
	public ResponseEntity<ApiResponse> doChangeArtwork(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "status", required = true) String status,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> artService.doChangeArtwork(authToken, artId, status));
		return response();
	}
	
	// Novos... (Documentar)
	@PostMapping("/addImageProduct")
	public ResponseEntity<ApiResponse> doAddArtImageProduct(
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "file", required = true) MultipartFile image,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.doAddArtImageProduct(authToken, image, artId));
		return response();
	}
	
	@PatchMapping("/changeVisibility")
	public ResponseEntity<ApiResponse> doChangeVisibility(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "visibility", required = true) String visibility,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.doChangeVisibility(authToken, artId, visibility));
		return response();
	}
}
