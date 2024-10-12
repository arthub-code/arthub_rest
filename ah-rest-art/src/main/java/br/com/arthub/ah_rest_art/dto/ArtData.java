package br.com.arthub.ah_rest_art.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.arthub.ah_rest_art.constants.ArtLevel;
import br.com.arthub.ah_rest_art.constants.ArtStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtData {
	private UUID artId;
	private String artName;
	private ArtLevel visibility;
	private ArtStatus status;
	private List<ArtImageRefData> imgRefs;
	private Boolean haveSchedule;
	private LocalDate startScheduleDate;
	private LocalDate endScheduleDate;
	private LocalDateTime createdAt;
	private String createdAtText;
	private LocalDateTime lastModified;
	private String lastModifiedText;
	
	public ArtData(UUID artId, String artName, 
			ArtLevel artLevel, ArtStatus artStatus, Boolean haveSchedule, 
            LocalDate startScheduleDate, LocalDate endScheduleDate, 
            LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.artId = artId;
		this.artName = artName;
		this.visibility = artLevel;
		this.status = artStatus;
		this.haveSchedule = haveSchedule;
		this.startScheduleDate = startScheduleDate;
		this.endScheduleDate = endScheduleDate;
		this.createdAt = createdAt;
		this.lastModified = updatedAt;
	}
}
