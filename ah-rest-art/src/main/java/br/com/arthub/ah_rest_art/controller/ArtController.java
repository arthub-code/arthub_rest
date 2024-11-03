package br.com.arthub.ah_rest_art.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.dto.AddOrUpdateArtImageRefPayload;
import br.com.arthub.ah_rest_art.dto.ApiResponse;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.dto.ClearArtImageRefsPayload;
import br.com.arthub.ah_rest_art.dto.UpdateArtPayload;
import br.com.arthub.ah_rest_art.service.ArtService;

@RestController
@RequestMapping("/v1")
public class ArtController extends ControllerModel {
	@Autowired
	private ArtService artService;
	
	// -- Artwork
	
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
	
	@GetMapping("/details/{artId}")
	public ResponseEntity<ApiResponse> detailsArt(@PathVariable(name = "artId") UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.detailsArtById(authToken, artId));
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
	
	@PatchMapping("/changeStatus")
	public ResponseEntity<ApiResponse> doChangeArtworkStatus(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "status", required = true) String status,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> artService.doChangeArtwork(authToken, artId, status));
		return response();
	}
	
	@PatchMapping("/changeVisibility")
	public ResponseEntity<ApiResponse> doChangeVisibility(@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "visibility", required = true) String visibility,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.doChangeVisibility(authToken, artId, visibility));
		return response();
	}
	
	// -- Image Reference
	
	@PostMapping("/imgReference/add")
	public ResponseEntity<ApiResponse> doAddArtImageRefs(@RequestBody AddOrUpdateArtImageRefPayload uploadArtImgRefPayload,
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken)  { 
		setOkResponse(() -> this.artService.doAddArtImagesRefs(artId, authToken, uploadArtImgRefPayload));
		return response();
	}
	
	@PutMapping("/imgReference/update")
	public ResponseEntity<ApiResponse> doUpdateAllArtImageRefs(@RequestBody AddOrUpdateArtImageRefPayload uploadArtImgRefPayload,
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> artService.doUpdateAllArtImageRefs(artId, authToken, uploadArtImgRefPayload));
		return response();
	}
	
	@PostMapping("/imgReference/delete")
	public ResponseEntity<ApiResponse> doClearArtImageReferences(
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestBody ClearArtImageRefsPayload payload,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.doClearArtImageReferences(authToken, artId, payload));
		return response();
	}
	
 	@GetMapping("/imgReference/get-image/{imgRefId}")
	public ResponseEntity<?> getImageRef(@PathVariable(name = "imgRefId") UUID imgRefId) throws IOException {
		setOkResponse(() -> this.artService.getImageRef(imgRefId));
		return responseImage();
	}
	
	// -- Image Product
	
	@PostMapping("/imgProduct/add")
	public ResponseEntity<ApiResponse> doAddArtImageProduct(
			@RequestParam(name = "artId", required = true) UUID artId,
			@RequestParam(name = "file", required = true) MultipartFile image,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.doAddArtImageProduct(authToken, image, artId));
		return response();
	}
	
	@PutMapping("/imgProduct/update/{artId}")
	public ResponseEntity<ApiResponse> updateImageProduct(
			@PathVariable(name = "artId") UUID artId,
			@RequestParam(name = "file") MultipartFile imageFile,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		setOkResponse(() -> this.artService.updateImageProductByArtId(authToken, artId, imageFile));
		return response();
	}
	
	@GetMapping("/imageProd/get-image/{artId}")
	public ResponseEntity<?> getImageProduct(@PathVariable(name = "artId") UUID artId) {
		setOkResponse(() -> this.artService.getImageProdByArtId(artId));
		return responseImage();
	}
	
	@DeleteMapping("/imgProduct/delete/{artId}")
	public ResponseEntity<?> doDeleteArtImageProduct(@PathVariable(name = "artId") UUID artId,
			@RequestHeader(name = "Authorization", required = true) String authToken) {
		try {
			artService.doDeleteImageProductByArtId(authToken, artId);
		} catch(Exception e ) {
			return ResponseEntity.status(BAD_REQUEST).body(
						new ApiResponse(BAD_REQUEST, true, e.getMessage())
					);
		}
		return ResponseEntity.noContent().build();
	}
}
