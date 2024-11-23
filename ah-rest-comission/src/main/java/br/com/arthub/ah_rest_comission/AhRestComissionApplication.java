package br.com.arthub.ah_rest_comission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AhRestComissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhRestComissionApplication.class, args);
	}

}
