package br.com.arthub.ah_rest_comission.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.arthub.ah_rest_comission.domain.enums.License;
import br.com.arthub.ah_rest_comission.domain.enums.NegotiationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComissionPayload {
	private String description;
	private BigDecimal totalAmount;
	private NegotiationType negotiationType;
	private License license;
	private int allowedRevisions;
	private LocalDateTime estimatedCompletionDate;
	private String goal;
}
