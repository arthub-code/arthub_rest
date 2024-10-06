package br.com.arthub.ah_rest_art.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import br.com.arthub.ah_rest_art.dto.ArtImageRefData;
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
		save(payload.getArtImageRef(), artParent, false);
	}
	
	public void updateImageRefToArt(List<ArtImageReferencePayload> refs) {
		save(refs, null, true);
	}
	
	private void save(List<ArtImageReferencePayload> refs, ArtEntity artParent, boolean update) {
		for(ArtImageReferencePayload refPayload : refs) {
			ArtImageReferenceEntity ref = new ArtImageReferenceEntity();
			if(artParent != null)
				ref.setArtParent(artParent);
			if(update) {
				ArtImageReferenceEntity refEntity = validateRefsId(refPayload);
				ref.setArtImageReferenceId(refPayload.getRefId());
				ref.setArtParent(refEntity.getArtParent());
			}
			
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
	
	public List<ArtImageRefData> getAllArtImgRefs(UUID artId) {
		return refRepository.getAllArtImgRefs(artId);
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
	
	public void doDeleteAllRefsIfExists(UUID artId) {
		for(ArtImageReferenceEntity ref : refRepository.findAllArtImgRefs(artId)) {
			refRepository.delete(ref);
		}
	}
	
	private String buildImageLink(UUID refId) {
		return "art/imageRef/get/" + refId;
	}
	
	private ArtImageReferenceEntity validateRefsId(ArtImageReferencePayload ref) {
		if(ref.getRefId() == null)
			throw new RuntimeException("Ref id field is required.");
		Optional<ArtImageReferenceEntity> opImgRef = refRepository.findById(ref.getRefId());
		if(opImgRef.isEmpty())
			throw new RuntimeException("image reference not found.");
		return opImgRef.get();
	}
}
