package br.com.arthub.ah_rest_art.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ArtPayload {
	private String artName;
	private Boolean haveSchedule;
	private UUID accountId;
	

	/** Atributos Opcionais */
	private LocalDate startScheduleDate;
	private LocalDate endScheduleDate;
	private List<ArtImageReferencePayload> artImageRef;
}
