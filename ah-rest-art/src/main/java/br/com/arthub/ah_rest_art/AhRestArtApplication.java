package br.com.arthub.ah_rest_art;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AhRestArtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhRestArtApplication.class, args);
	}

}
