package br.com.arthub.ah_rest_art.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageReferenceRepository;

@Service
public class ArtImageReferenceService {
	@Autowired
	private ArtImageReferenceRepository refRepository;
	
	public void createImageRefToArt(ArtPayload payload, ArtEntity artParent) {
		ArtImageReferenceEntity ref = new ArtImageReferenceEntity();
		ref.setArtParent(artParent);
		if(payload.getArtImageRef().getImageBytes() != null && payload.getArtImageRef().getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD)
			ref.setImageBytes(payload.getArtImageRef().getImageBytes());

		if(payload.getArtImageRef().getUploadType() == ArtImageReferenceUploadType.PINTEREST_API 
				&& !payload.getArtImageRef().getImageLink().isBlank()) {
			ref.setImageLink(payload.getArtImageRef().getImageLink());
			refRepository.saveAndFlush(ref);
		} else {
			ArtImageReferenceEntity registred = refRepository.saveAndFlush(ref);
			registred.setImageLink(buildImageLink(registred.getArtImageReferenceId()));
			refRepository.saveAndFlush(registred);
		}
	}
	
	public void validateImgRefPayload(ArtPayload payload) {
		 if(payload.getArtImageRef() == null)
			 throw new RuntimeException("art image reference field is required.");
		 if(payload.getArtImageRef().getUploadType() == null)
			 throw new RuntimeException("\"upload type\" inf \"art image reference\" is required.");
		 if((payload.getArtImageRef().getImageBytes() != null && payload.getArtImageRef().getUploadType() != ArtImageReferenceUploadType.DEVICE_UPLOAD)
		    || (payload.getArtImageRef().getImageBytes() != null && payload.getArtImageRef().getUploadType() == ArtImageReferenceUploadType.PINTEREST_API))
			 throw new RuntimeException("The upload type is not valid for this situation.");
	}
	
	private String buildImageLink(UUID refId) {
		return "art/imageRef/get/" + refId;
	}
}
