package org.projects.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/users/**")
                        .uri("lb://auth-service"))
                .route("history-order-service", r -> r.path("/api/order-history/**")
                        .uri("lb://history-order-service"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .uri("lb://order-service"))
                .route("instrument-service", r -> r.path("/api/instruments/**")
                        .uri("lb://instrument-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .uri("lb://inventory-service"))
                .route("eureka-service", r -> r.path("/eureka")
                        .filters(f -> f.rewritePath("/eureka", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-static", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
