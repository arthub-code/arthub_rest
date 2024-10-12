package br.com.arthub.ah_rest_art.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArtPayload {
	private String artName;
	private Boolean haveSchedule;

	/** Atributos Opcionais */
	private LocalDate startScheduleDate;
	private LocalDate endScheduleDate;
}
