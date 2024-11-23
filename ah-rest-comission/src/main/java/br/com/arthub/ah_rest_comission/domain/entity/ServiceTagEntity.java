package br.com.arthub.ah_rest_comission.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "service_tags")
@Data
public class ServiceTagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "tag_id")
	private UUID tagId;
	
	@Column(nullable = false)
	private String tag;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "service_fk")
	private ServiceEntity serviceParent;
	
	public ServiceTagEntity() {
		
	}
	
	public ServiceTagEntity(String tag, ServiceEntity serviceParent) {
		this.createdAt = LocalDateTime.now();
		this.tag = tag;
		this.serviceParent = serviceParent;
	}
}
