package br.com.arthub.ah_rest_useraccount.api.v1.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailAlreadyInUseException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.EmailInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.PasswordIsInvalidException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameAlreadyInUseException;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UsernameIsInvalidException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestGlobalExceptionHandler {
	private final static HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
	private final static HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
	
	@ExceptionHandler({ UsernameAlreadyInUseException.class })
	public ResponseEntity<ApiResponse> usernameAlreadyInUseException(UsernameAlreadyInUseException e) {
		return buildError(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler({ UsernameIsInvalidException.class })
	public ResponseEntity<ApiResponse> usernameIsInvalidException(UsernameIsInvalidException e) {
		return buildError(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler({ PasswordIsInvalidException.class })
	public ResponseEntity<ApiResponse> passwordIsInvalidException(PasswordIsInvalidException e) {
		return buildError(BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler({ EmailAlreadyInUseException.class })
	public ResponseEntity<ApiResponse> emailAlreadyInUseException(EmailAlreadyInUseException e) {
		return buildError(BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler({ EmailInvalidException.class })
	public ResponseEntity<ApiResponse> emailInvalidException(EmailInvalidException e) {
		return buildError(BAD_REQUEST, e.getMessage());
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
