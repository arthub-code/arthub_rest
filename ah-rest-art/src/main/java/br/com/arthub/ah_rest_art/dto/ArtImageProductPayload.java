package br.com.arthub.ah_rest_art.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ArtImageProductPayload {
	private byte[] imageBytes;
	private UUID artParentid;
}
