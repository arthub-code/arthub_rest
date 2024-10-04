package br.com.arthub.ah_rest_art.feign.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.arthub.ah_rest_art.config.FeignClientConfig;
import br.com.arthub.ah_rest_art.dto.ApiResponse;

@Service
@FeignClient(name = "ah-rest-useraccount", configuration = FeignClientConfig.class)
public interface UserAccountFeignClient {
	@GetMapping("/v1/accountExists")
	ResponseEntity<ApiResponse> userAccountExists(@RequestParam UUID accountId);
}
