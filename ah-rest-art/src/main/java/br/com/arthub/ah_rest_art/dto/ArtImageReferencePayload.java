package br.com.arthub.ah_rest_art.dto;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import lombok.Data;

@Data
public class ArtImageReferencePayload {
	private ArtImageReferenceUploadType uploadType;
	
	/** Atributos opcionais */
	private byte[] imageBytes;
}
