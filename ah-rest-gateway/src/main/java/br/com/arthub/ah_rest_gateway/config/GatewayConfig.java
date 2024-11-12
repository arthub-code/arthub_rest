package br.com.arthub.ah_rest_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.arthub.ah_rest_gateway.filters.AuthenticationFilter;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("useraccount_route", r -> r.path("/useraccount/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://ah-rest-useraccount"))
            .route("email_route", r -> r.path("/email/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://ah-rest-email"))
            .route("art_route", r -> r.path("/art/**")
            	.filters(f -> f.stripPrefix(1))
            	.uri("lb://ah-rest-art"))
            .route("comission_route", r -> r.path("/comission/**")
                	.filters(f -> f.stripPrefix(1))
                	.uri("lb://ah-rest-comission"))
            .build();
    }
}
