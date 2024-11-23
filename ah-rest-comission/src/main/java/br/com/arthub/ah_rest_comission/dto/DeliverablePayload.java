package br.com.arthub.ah_rest_comission.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliverablePayload {
	private String name;
	private String description;
	private LocalDateTime expectedDeliveryDate;
	private MultipartFile file;
	private BigDecimal paymentAmount;
	private UUID artist;
}
