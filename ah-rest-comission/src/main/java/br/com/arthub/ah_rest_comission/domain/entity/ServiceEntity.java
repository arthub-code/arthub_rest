package br.com.arthub.ah_rest_comission.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import br.com.arthub.ah_rest_comission.domain.enums.ServiceType;
import br.com.arthub.ah_rest_comission.dto.ServicePayload;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "services")
@Data
public class ServiceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "service_id")
	private UUID serviceId;
	
	@Column(nullable = false, length = 60)
	private String name;
	
	@Column(nullable = false)
	@Lob
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ServiceType type;
	
	@Column(name = "service_amount", nullable = false)
	private BigDecimal serviceAmount;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "last_modified_at", nullable = false)
	private LocalDateTime lastModifiedAt;
	
	// Id de usuário do artista, nesse casso a conta deve ser obrigatóriamente do tipo artista.
	@Column(name = "provided_by", nullable = false)
	private UUID providedBy;
	
	@OneToMany(mappedBy = "serviceParent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	Set<ComissionEntity> comissions;
	
	@OneToMany(mappedBy = "serviceParent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	Set<ServiceTagEntity> tags;
	
	public ServiceEntity() {
		
	}
	
	public ServiceEntity(ServicePayload payload) {
		start();
		this.name = payload.getName();
		this.description = payload.getDescription();
		this.type = payload.getType();
		this.serviceAmount = payload.getServiceAmount();
		this.providedBy = payload.getArtistId();
	}
	
	private void start() {
		this.createdAt = LocalDateTime.now();
		this.lastModifiedAt = LocalDateTime.now();
	}
}
