package br.com.arthub.ah_rest_art.controller;

import java.util.concurrent.Callable;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.dto.ApiResponse;


public class ControllerModel {
    protected ApiResponse apiResponse;

    protected static final int CREATED     = HttpStatus.CREATED.value(),
                               BAD_REQUEST = HttpStatus.BAD_REQUEST.value(),
                               OK          = HttpStatus.OK.value(),
                               NO_CONTENT  = HttpStatus.NO_CONTENT.value(),
                               UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

    protected ResponseEntity<ApiResponse> response() {
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    protected ResponseEntity<?> responseImage() { 
    	if(apiResponse.isHasErrors())
			return response();
		
		MultipartFile file = (MultipartFile) apiResponse.getData();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(file.getContentType()));
	    headers.setContentLength(file.getSize());

	    byte[] bytes = {};
	    try {
	    	bytes = file.getBytes();
	    } catch(Exception e) {
	    	return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(BAD_REQUEST, true, e.getMessage()));
	    }
	    return new ResponseEntity<>(bytes, headers, OK);
    }

    protected ResponseEntity<Void> responseNoContent() {
        return ResponseEntity.status(NO_CONTENT).build();
    }

    protected void setResponse(Callable<Object> valueCallable, int status) {
        try {
            Object value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value);
        }  catch (Exception e) {
            this.apiResponse = new ApiResponse(BAD_REQUEST, true, e.getMessage());
        }
    }

    protected void setResponse(Callable<Object> valueCallable, int status, int errorStatus) {
        try {
            Object value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value);
        } catch(Exception e) {
            this.apiResponse = new ApiResponse(errorStatus, true, e.getMessage());
        }
    }

    protected void setCreatedResponse(Callable<Object> value) {
        setResponse(value, CREATED);
    }

    protected void setOkResponse(Callable<Object> value) {
        setResponse(value, OK);
    }
}
