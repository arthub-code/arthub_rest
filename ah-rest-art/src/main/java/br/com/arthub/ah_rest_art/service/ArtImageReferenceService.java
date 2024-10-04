package br.com.arthub.ah_rest_art.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageReferenceRepository;

@Service
public class ArtImageReferenceService {
	@Autowired
	private ArtImageReferenceRepository refRepository;
	
	public void createImageRefToArt(ArtPayload payload, UUID artId) {
		validateImgRefPayload(payload);
		ArtImageReferenceEntity ref = new ArtImageReferenceEntity();
		if(payload.getArtImageRef().getImageBytes() != null)
			ref.setImageBytes(payload.getArtImageRef().getImageBytes());
	}
	
	public void validateImgRefPayload(ArtPayload payload) {
		 if(payload.getArtImageRef() == null)
			 throw new RuntimeException("art image reference field is required.");
		 if(payload.getArtImageRef().getUploadType() == null)
			 throw new RuntimeException("\"upload type\" inf \"art image reference\" is required.");
	}
}
