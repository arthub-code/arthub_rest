package br.com.arthub.ah_rest_art.entity;

import java.util.UUID;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "art_image_references")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageReferenceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "art_image_reference_id")
	private UUID artImageReferenceId;
	
	@Column(name = "image_bytes")
	@Lob
	private byte[] imageBytes;
	
	private String contentType;
	
	private String fileName;
	
	@Column(name = "image_link")
	private String imageLink;
	
	@Column(name = "upload_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ArtImageReferenceUploadType uploadType;
	
	/** Relacionamentos */
	@ManyToOne
	@JoinColumn(name = "art_parent_fk")
	private ArtEntity artParent;
}
