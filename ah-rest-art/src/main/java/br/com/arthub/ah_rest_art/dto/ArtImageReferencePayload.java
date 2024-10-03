package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import lombok.Data;

@Data
public class ArtImageReferencePayload {
	private ArtImageReferenceUploadType uploadType;
	private UUID artParentid;
	
	/** Atributos opcionais */
	private byte[] imageBytes;
}
