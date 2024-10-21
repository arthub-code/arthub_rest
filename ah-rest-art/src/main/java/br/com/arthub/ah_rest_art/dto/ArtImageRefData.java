package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtImageRefData {
	private UUID imgRefId;
	private String imageLink;
	private boolean isExternalUpload;
	
	public ArtImageRefData(UUID imgRefId, String imageLink, ArtImageReferenceUploadType uploadType) {
		this.imgRefId = imgRefId;
		this.imageLink = imageLink;
		this.isExternalUpload = uploadType != ArtImageReferenceUploadType.DEVICE_UPLOAD;
	}
}
