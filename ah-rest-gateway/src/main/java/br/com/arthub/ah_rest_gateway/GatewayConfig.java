package br.com.arthub.ah_rest_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            .build();
    }
}
