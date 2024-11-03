package br.com.arthub.ah_rest_art.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import br.com.arthub.ah_rest_art.constants.ContentType;
import br.com.arthub.ah_rest_art.dto.AddOrUpdateArtImageRefPayload;
import br.com.arthub.ah_rest_art.dto.ArtImageRefData;
import br.com.arthub.ah_rest_art.dto.ArtImageReferencePayload;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.dto.ClearArtImageRefsPayload;
import br.com.arthub.ah_rest_art.dto.FileData;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageReferenceRepository;
import br.com.arthub.ah_rest_art.utils.MultipartFileUtils;

@Service
public class ArtImageReferenceService {
	@Autowired
	private ArtImageReferenceRepository refRepository;
	
	public void createImageRefToArt(ArtPayload payload, ArtEntity artParent) {
		save(payload.getArtImageRef(), artParent, false);
	}
	
	public void doAddArtImageReferences(List<ArtImageReferencePayload> refs, ArtEntity art) {
		save(refs, art, false);
	}
	
	public void updateImageRefToArt(List<ArtImageReferencePayload> refs) {
		save(refs, null, true);
	}
	
	public MultipartFile getImageRef(UUID id) {
		Optional<ArtImageReferenceEntity> op = this.refRepository.findById(id);
		if(op.isEmpty())
			throw new RuntimeException("Image not found.");
		
		if(op.get().getUploadType() != ArtImageReferenceUploadType.DEVICE_UPLOAD)
			throw new RuntimeException("Invalid upload type for this operation");
		
		FileData fileData = new FileData();
		fileData.setFileName(op.get().getFileName());
		fileData.setContentType(op.get().getContentType());
		
		return MultipartFileUtils.convertToMultipartfile(fileData, op.get().getImageBytes());
	}
	
	private void save(List<ArtImageReferencePayload> refs, ArtEntity artParent, boolean update) {
	    for (ArtImageReferencePayload refPayload : refs) {
	        ArtImageReferenceEntity ref = new ArtImageReferenceEntity();
	        if (artParent != null) {
	            ref.setArtParent(artParent);
	        }
	        if (update) {
	            ArtImageReferenceEntity refEntity = validateRefsId(refPayload);
	            ref.setArtImageReferenceId(refPayload.getRefId());
	            ref.setArtParent(refEntity.getArtParent());
	        }

	        ref.setUploadType(refPayload.getUploadType());

	        FileData fileData = refPayload.getFileData();
	        if (validateFileData(fileData) && refPayload.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD) {
	        	MultipartFileUtils.validateContentTypes(fileData, ContentType.PNG, ContentType.JPEG, ContentType.WEBP);
	        	try {
		        	byte[] imageBytes = Base64.getDecoder().decode(fileData.getBase64());
		        	ref.setImageBytes(imageBytes);
		        	ref.setFileName(fileData.getFileName());
		        	ref.setContentType(fileData.getContentType());
	        	} catch (Exception e) {
	                throw new RuntimeException("Error decoding Base64: " + e.getMessage());
	            } 
	        }

	        
	        if(refPayload.getUploadType() == ArtImageReferenceUploadType.PINTEREST_API 
	                && (refPayload.getImageLink() == null || refPayload.getImageLink().equals(""))) 
	        	throw new RuntimeException("Image link is required.");
	        
	        	if (refPayload.getUploadType() == ArtImageReferenceUploadType.PINTEREST_API 
	                && refPayload.getImageLink() != null) {
	            ref.setImageLink(refPayload.getImageLink());
	            refRepository.saveAndFlush(ref);
	        } else {
	            ArtImageReferenceEntity registred = refRepository.saveAndFlush(ref);
	            registred.setImageLink(buildImageLink(registred.getArtImageReferenceId()));
	            refRepository.saveAndFlush(registred);
	        }
	    }
	}

	
	public void clear(ClearArtImageRefsPayload payload, UUID artId) {
		if(payload.isClearAll()) {
			this.refRepository.clearAll(artId);
			return;
		}
		
		if(payload.getRefsId().isEmpty())
			throw new RuntimeException("Unable to clean reference images, the list provided is empty.");
		
		for(UUID refId : payload.getRefsId()) {
			Optional<ArtImageReferenceEntity> opImg = this.refRepository.findById(refId);
			if(opImg.isEmpty())
				throw new RuntimeException("Reference not found. There are wrong ids in the reference list.");
			
			this.refRepository.delete(opImg.get());
		}
	}
	 
	public List<ArtImageRefData> getAllArtImgRefs(UUID artId) {
		return refRepository.getAllArtImgRefs(artId);
	}
	
	public void validateImgRefPayload(ArtPayload payload) {
	    if (payload.getArtImageRef() == null) {
	        throw new RuntimeException("art image reference field is required.");
	    }

	    for (ArtImageReferencePayload refPayload : payload.getArtImageRef()) {
	        if (refPayload.getUploadType() == null) {
	            throw new RuntimeException("\"upload type\" in \"art image reference\" is required.");
	        }

	        // Obter o objeto fileData
	        FileData fileData = refPayload.getFileData();

	        // Validação para upload de arquivo ou link
	        if ((fileData != null && fileData.getBase64() != null && refPayload.getUploadType() != ArtImageReferenceUploadType.DEVICE_UPLOAD)
	            || (fileData != null && fileData.getBase64() != null && refPayload.getUploadType() == ArtImageReferenceUploadType.PINTEREST_API)
	            || (refPayload.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD && refPayload.getImageLink() != null)) {
	            throw new RuntimeException("The upload type is not valid for this situation.");
	        }

	        // Validações adicionais para o objeto fileData, se necessário
	        if (refPayload.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD) {
	            if (fileData == null || fileData.getBase64() == null || fileData.getFileName() == null || fileData.getContentType() == null) {
	                throw new RuntimeException("File data is required for device uploads.");
	            }
	        }
	    }
	}

	
	public void doDeleteAllRefsIfExists(UUID artId) {
		for(ArtImageReferenceEntity ref : refRepository.findAllArtImgRefs(artId)) {
			refRepository.delete(ref);
		}
	}
	
	private String buildImageLink(UUID refId) {
		return "imageRef/get/" + refId;
	}
	
	private boolean validateFileData(FileData fileData) {
		return fileData != null && fileData.getBase64() != null && fileData.getFileName() != null && fileData.getContentType() != null;
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
