package br.com.arthub.ah_rest_comission.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import br.com.arthub.ah_rest_comission.domain.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePayload {
	private String name;
	private String description;
	private ServiceType type;
	private BigDecimal serviceAmount;
	private List<String> tags;
	private boolean bAutomatic = false;
	private UUID artistId;
}
