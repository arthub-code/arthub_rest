package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtImageReferencePayload {
	private ArtImageReferenceUploadType uploadType;
	
	/** Atributos opcionais */
	private UUID refId;
	private FileData fileData;
	private String imageLink;
}
