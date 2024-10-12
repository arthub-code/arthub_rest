package br.com.arthub.ah_rest_art.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import br.com.arthub.ah_rest_art.constants.ArtLevel;
import br.com.arthub.ah_rest_art.constants.ArtStatus;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

@Entity
@Table(name = "arts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "art_id")
	private UUID artId;
	@Column(name = "art_name", length = 80, nullable = false)
	private String artName;
	
	@Column(name = "art_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ArtStatus artStatus;
	
	@Column(name = "art_level", nullable = false)
	@Enumerated(EnumType.STRING)
	private ArtLevel artLevel; // nível da arte: Pública, Privada, Não listado, etc.
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@Column(name = "have_schedule", nullable = false)
	private Boolean haveSchedule;
	
	@Column(name = "start_schedule_date")
	private LocalDate startScheduleDate; 
	
	@Column(name = "end_schedule_date")
	private LocalDate endScheduleDate;
	
	@Column(name = "user_account_id", nullable = false)
	private UUID userAccountId;

	@OneToMany(mappedBy = "artParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ArtImageReferenceEntity> imageReferences;
	
	@OneToOne(mappedBy = "artParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ArtImageProductEntity imageProduct;
	
	public ArtEntity(ArtPayload payload) {
		start();
		this.artName = payload.getArtName();
		this.haveSchedule = payload.getHaveSchedule();
		if(this.haveSchedule) {
			this.startScheduleDate = payload.getStartScheduleDate();
			this.endScheduleDate = payload.getEndScheduleDate();
		}
	}
	
	private void start() {
		this.artLevel = ArtLevel.PRIVATE;
		this.artStatus = ArtStatus.TODO;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
}
