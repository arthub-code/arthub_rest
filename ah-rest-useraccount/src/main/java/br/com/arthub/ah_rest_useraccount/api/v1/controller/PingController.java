package br.com.arthub.ah_rest_useraccount.api.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.ApiResponse;

@RestController
public class PingController extends ControllerModel {
    @GetMapping("ping")
    public ResponseEntity<ApiResponse> doPing() {
        setOkResponse(() -> "Pong!");
        return response();
    }
}
