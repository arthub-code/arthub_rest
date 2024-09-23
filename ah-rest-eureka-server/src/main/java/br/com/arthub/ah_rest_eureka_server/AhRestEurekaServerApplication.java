package br.com.arthub.ah_rest_eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AhRestEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhRestEurekaServerApplication.class, args);
	}

}
