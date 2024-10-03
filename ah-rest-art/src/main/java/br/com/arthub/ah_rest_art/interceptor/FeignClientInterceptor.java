package br.com.arthub.ah_rest_art.interceptor;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
	private final HttpServletRequest request;
	
	public FeignClientInterceptor(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void apply(RequestTemplate reqTemplate) {
		String authToken = request.getHeader("Authorization");
		if(authToken != null && authToken.startsWith("Bearer ")) {
			reqTemplate.header("Authorization", authToken);
		}
	}
}
