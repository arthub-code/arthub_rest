package br.com.arthub.ah_rest_comission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
	private int status;
    private boolean hasErrors;
    private Object data;
    private String message;
}
