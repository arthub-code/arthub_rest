package br.com.arthub.ah_rest_comission.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.arthub.ah_rest_comission.domain.enums.ComissionStatus;
import br.com.arthub.ah_rest_comission.domain.enums.License;
import br.com.arthub.ah_rest_comission.domain.enums.NegotiationType;
import br.com.arthub.ah_rest_comission.dto.ComissionPayload;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "comissions")
@Data
public class ComissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "comission_id")
	private UUID comissionId;
	
	@Lob
	private String description;
	
	@Column(nullable = false)
	private String goal;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "negotiation_type", nullable = false)
	private NegotiationType negotiationType;
	
	@Column(nullable = false)
	private License license;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ComissionStatus status;
	 
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "last_modified_at", nullable = false)
	private LocalDateTime lastModifiedAt;
	
	@Column(name = "allowed_revisions", nullable = false)
	private int allowedRevisions;
	
	@Column(name = "completed_revisions", nullable = false)
	private int completedRevisions;
	
	@Column(name = "estimated_completion_date", nullable = false)
	private LocalDateTime estimatedCompletionDate;
	
	@OneToMany(mappedBy = "comissionParent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeliverableEntity> deliverables;
	
	@ManyToOne
	@JoinColumn(name = "service_fk")
	private ServiceEntity serviceParent;
	
	public ComissionEntity() {
		
	}
	
	public ComissionEntity(ComissionPayload payload) {
		start();
		this.description = payload.getDescription();
		this.totalAmount = payload.getTotalAmount();
		this.negotiationType = payload.getNegotiationType();
		this.license = payload.getLicense();
		this.allowedRevisions = payload.getAllowedRevisions();
		this.estimatedCompletionDate = payload.getEstimatedCompletionDate();
	}
	
	private void start() {
		this.createdAt = LocalDateTime.now();
		this.lastModifiedAt = LocalDateTime.now();
		this.completedRevisions = 0;
	}
}
