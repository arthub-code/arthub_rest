package br.com.arthub.ah_rest_comission.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceEntity;
import br.com.arthub.ah_rest_comission.domain.enums.ServiceType;
import br.com.arthub.ah_rest_comission.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceData {
	private UUID serviceId;
	private String name;
	private String description;
	private ServiceType type;
	private BigDecimal amount;
	private LocalDateTime createdAt;
	private String createdAtText;
	private LocalDateTime lastModified;
	private String lastModifiedText;
	private List<String> tags;

	public ServiceData(ServiceEntity service, List<String> tags) {
		this.serviceId = service.getServiceId();
		this.name = service.getName();
		this.description = service.getDescription();
		this.type = service.getType();
		this.amount = service.getServiceAmount();
		this.createdAt = service.getCreatedAt();
		this.createdAtText = DateUtils.timeAgo(service.getCreatedAt());
		this.lastModified = service.getLastModifiedAt();
		this.lastModifiedText = DateUtils.timeAgo(service.getLastModifiedAt());
		this.tags = tags;
	}
}
