package br.com.arthub.ah_rest_art.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrUpdateArtImageRefPayload {
	private List<ArtImageReferencePayload> artImageRef;
}
