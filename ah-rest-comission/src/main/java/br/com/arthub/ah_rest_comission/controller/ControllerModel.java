package br.com.arthub.ah_rest_comission.controller;

import java.util.concurrent.Callable;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_comission.dto.ApiResponse;
import br.com.arthub.ah_rest_comission.dto.GenericResponse;

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
	    	return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(BAD_REQUEST, true, null, e.getMessage()));
	    }
	    return new ResponseEntity<>(bytes, headers, OK);
    }

    protected ResponseEntity<Void> responseNoContent() {
        return ResponseEntity.status(NO_CONTENT).build();
    }

    protected void setResponse(Callable<GenericResponse> valueCallable, int status) {
        try {
        	GenericResponse value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value.getData(), value.getMessage());
        }  catch (Exception e) {
            this.apiResponse = new ApiResponse(BAD_REQUEST, true, null, e.getMessage());
        }
    }

    protected void setResponse(Callable<GenericResponse> valueCallable, int status, int errorStatus) {
        try {
        	GenericResponse value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value.getData(), value.getMessage());
        } catch(Exception e) {
            this.apiResponse = new ApiResponse(errorStatus, true, null, e.getMessage());
        }
    }

    protected void setCreatedResponse(Callable<GenericResponse> value) {
        setResponse(value, CREATED);
    }

    protected void setOkResponse(Callable<GenericResponse> value) {
        setResponse(value, OK);
    }
}
