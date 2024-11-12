package br.com.arthub.ah_rest_comission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ComissionController {
	@GetMapping
	public ResponseEntity<String> sayHi() {
		return ResponseEntity.ok("Hi! <3");
	}
}
