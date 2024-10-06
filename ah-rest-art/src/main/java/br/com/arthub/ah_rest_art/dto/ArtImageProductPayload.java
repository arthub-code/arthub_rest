package br.com.arthub.ah_rest_art.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtImageProductPayload {
	private byte[] imageBytes;
}
