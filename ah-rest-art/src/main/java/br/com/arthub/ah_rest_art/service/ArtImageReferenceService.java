package br.com.arthub.ah_rest_art.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import br.com.arthub.ah_rest_art.dto.ArtImageReferencePayload;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageReferenceRepository;

@Service
public class ArtImageReferenceService {
	@Autowired
	private ArtImageReferenceRepository refRepository;
	
	public void createImageRefToArt(ArtPayload payload, ArtEntity artParent) {
		for(ArtImageReferencePayload refPayload : payload.getArtImageRef()) {
			ArtImageReferenceEntity ref = new ArtImageReferenceEntity();
			ref.setArtParent(artParent);
			ref.setUploadType(refPayload.getUploadType());
			if(refPayload.getImageBytes() != null && refPayload.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD)
				ref.setImageBytes(refPayload.getImageBytes());

			if(refPayload.getUploadType() == ArtImageReferenceUploadType.PINTEREST_API 
					&& !refPayload.getImageLink().isBlank()) {
				ref.setImageLink(refPayload.getImageLink());
				refRepository.saveAndFlush(ref);
			} else {
				ArtImageReferenceEntity registred = refRepository.saveAndFlush(ref);
				registred.setImageLink(buildImageLink(registred.getArtImageReferenceId()));
				refRepository.saveAndFlush(registred);
			}
		}
	}
	
	public void validateImgRefPayload(ArtPayload payload) {
		if(payload.getArtImageRef() == null)
			throw new RuntimeException("art image reference field is required.");
		
		for(ArtImageReferencePayload refPayload : payload.getArtImageRef()) {
			if(refPayload.getUploadType() == null)
				throw new RuntimeException("\"upload type\" inf \"art image reference\" is required.");
			if((refPayload.getImageBytes() != null && refPayload.getUploadType() != ArtImageReferenceUploadType.DEVICE_UPLOAD)
			   || (refPayload.getImageBytes() != null && refPayload.getUploadType() == ArtImageReferenceUploadType.PINTEREST_API)
			   || (refPayload.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD && !refPayload.getImageLink().isBlank()))
				throw new RuntimeException("The upload type is not valid for this situation.");
		}
	}
	
	private String buildImageLink(UUID refId) {
		return "art/imageRef/get/" + refId;
	}
}
