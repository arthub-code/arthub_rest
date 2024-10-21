package br.com.arthub.ah_rest_gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arthub.ah_rest_gateway.dto.ApiResponse;
import br.com.arthub.ah_rest_gateway.dto.LoginResponse;
import br.com.arthub.ah_rest_gateway.exception.ApiResponseException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().value().contains("/public") || 
        	exchange.getRequest().getPath().value().contains("/actuator/health") ||
        	exchange.getRequest().getPath().value().contains("get-image")) {
            return chain.filter(exchange);
        }

        String headerAuthToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (headerAuthToken == null) {
        	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        	return exchange.getResponse().setComplete();
        }
        
        String token = exchange.getRequest().getHeaders().getFirst("Authorization").replace("Bearer ", "").trim();
        
        return webClientBuilder.build()
        	    .get()
        	    .uri("lb://ah-rest-useraccount/v1/public/validateToken?token=" + token)
        	    .retrieve()
        	    .onStatus(HttpStatusCode::isError, clientResponse -> 
        	        clientResponse.bodyToMono(String.class)
        	            .flatMap(errorMessage ->  {
        	            	ApiResponse apiResponse = null;
        	            	try {
								apiResponse = objectMapper.readValue(errorMessage, ApiResponse.class);
								LoginResponse loginResponse = null;
								loginResponse = (LoginResponse) apiResponse.getData();
								apiResponse.setData(loginResponse);
							} 
        	            	catch (JsonMappingException e) {
								return Mono.error(new RuntimeException(errorMessage));
							} catch (JsonProcessingException e) {
								return Mono.error(new RuntimeException(errorMessage));
							} catch (Exception e) {
        	            		return Mono.error(new RuntimeException(errorMessage));
							}
        	            	return Mono.just(new ApiResponseException(apiResponse));
        	            })
        	    )
        	    .bodyToMono(String.class)
        	    .flatMap(response -> {
        	        return chain.filter(exchange);
        	    })
        	    .onErrorResume(e -> {
        	        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        	        return exchange.getResponse().setComplete();
        	    });
    }
}
