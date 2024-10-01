package br.com.arthub.ah_rest_useraccount.api.v1.controller;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;
import br.com.arthub.ah_rest_useraccount.api.v1.exception.UnauthorizatedException;

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

    protected ResponseEntity<Void> responseNoContent() {
        return ResponseEntity.status(NO_CONTENT).build();
    }

    protected void setResponse(Callable<Object> valueCallable, int status) {
        try {
            Object value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value);
        } catch (UnauthorizatedException e) {
        	this.apiResponse = new ApiResponse(UNAUTHORIZED, true, e.getResponse());
        } catch (Exception e) {
            this.apiResponse = new ApiResponse(BAD_REQUEST, true, e.getMessage());
        }
    }

    protected void setResponse(Callable<Object> valueCallable, int status, int errorStatus) {
        try {
            Object value = valueCallable.call();
            this.apiResponse = new ApiResponse(status, false, value);
        } catch (UnauthorizatedException e) {
        	this.apiResponse = new ApiResponse(errorStatus, true, e.getResponse());
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
