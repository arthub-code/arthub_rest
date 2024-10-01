package br.com.arthub.ah_rest_gateway.exception;

import br.com.arthub.ah_rest_gateway.dto.ApiResponse;

public class ApiResponseException extends RuntimeException {
    private final ApiResponse apiResponse;

    public ApiResponseException(ApiResponse apiResponse) {
        super("API response contains errors: " + apiResponse.getData());
        this.apiResponse = apiResponse;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}