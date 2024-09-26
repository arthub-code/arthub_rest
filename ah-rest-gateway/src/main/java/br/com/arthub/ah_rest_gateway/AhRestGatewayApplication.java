package br.com.arthub.ah_rest_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AhRestGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhRestGatewayApplication.class, args);
	}

}

@RestController
class PingController {
	@GetMapping("ping")
	public ResponseEntity<String> doPing() {
		return ResponseEntity.ok("Pong!");
	}
}