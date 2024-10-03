package br.com.arthub.ah_rest_art.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "art_image_product_id")
	private UUID artImageProductId;
	
	@Column(name = "image_bytes", nullable = false)
	@Lob
	private byte[] imageBytes;
	
	@Column(name = "image_link")
	private String imageLink;
	
	/** Relacionamentos */
	@OneToOne
	@JoinColumn(name = "art_parent_fk")
	private ArtEntity artParent;
}
