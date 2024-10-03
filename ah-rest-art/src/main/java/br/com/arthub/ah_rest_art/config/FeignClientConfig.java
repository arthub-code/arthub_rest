package br.com.arthub.ah_rest_art.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.arthub.ah_rest_art.interceptor.FeignClientInterceptor;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientConfig {
	@Bean
	public RequestInterceptor feignClientInterceptor(HttpServletRequest request) {
		return new FeignClientInterceptor(request);
	}
}
