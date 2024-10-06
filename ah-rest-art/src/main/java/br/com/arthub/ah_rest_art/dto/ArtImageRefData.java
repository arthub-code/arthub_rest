package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtImageRefData {
	private UUID imgRefId;
	private String imageLink;
	
	public ArtImageRefData(UUID imgRefId, String imageLink) {
		this.imgRefId = imgRefId;
		this.imageLink = imageLink;
	}
}
