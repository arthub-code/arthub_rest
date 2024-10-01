package br.com.arthub.ah_rest_gateway.config;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder(ReactorLoadBalancerExchangeFilterFunction lbFunction ) {
        return WebClient.builder().filter(lbFunction);
    }
}
