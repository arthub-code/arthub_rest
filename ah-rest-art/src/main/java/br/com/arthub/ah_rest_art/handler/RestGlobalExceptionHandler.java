package br.com.arthub.ah_rest_art.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import br.com.arthub.ah_rest_art.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestGlobalExceptionHandler {
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxUploadSize;
	private final static HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
	private final static HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
	
	@ExceptionHandler({ MaxUploadSizeExceededException.class })
	public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		return buildError(BAD_REQUEST, "The file is too large. Try uploading a smaller file, the maximum size is " + maxUploadSize);
	}
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception e) {
        log.error("Erro inesperado na API: ", e);
        return buildError(INTERNAL_SERVER_ERROR, e.getMessage());
    }
	
	private ResponseEntity<ApiResponse> buildError(HttpStatus status, Object data) {
		return ResponseEntity.status(status).body(new ApiResponse(
				status.value(),
				true,
				data
				));
	}
}
