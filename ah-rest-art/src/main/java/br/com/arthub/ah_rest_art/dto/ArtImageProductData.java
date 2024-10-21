package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import br.com.arthub.ah_rest_art.entity.ArtImageProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageProductData {
	private UUID imgProductId;
	private String imageLink;
	
	public ArtImageProductData(ArtImageProductEntity entity) {
		this.imgProductId = entity.getArtImageProductId();
		this.imageLink = entity.getImageLink();
	}
}
