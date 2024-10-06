package br.com.arthub.ah_rest_art.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtPayload {
	private String artName;
	private Boolean haveSchedule;	

	/** Atributos Opcionais */
	private LocalDate startScheduleDate;
	private LocalDate endScheduleDate;
	private List<ArtImageReferencePayload> artImageRef;
	
	public ArtPayload(UpdateArtPayload updatePayload) {
		this.artName = updatePayload.getArtName();
		this.haveSchedule = updatePayload.getHaveSchedule();
		this.startScheduleDate = updatePayload.getStartScheduleDate();
		this.endScheduleDate = updatePayload.getEndScheduleDate();
		this.artImageRef = null;
	}
}
