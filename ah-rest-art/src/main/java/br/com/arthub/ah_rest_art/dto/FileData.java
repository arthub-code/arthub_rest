package br.com.arthub.ah_rest_art.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {
	private String base64;
	private String fileName;
	private String contentType;
}
