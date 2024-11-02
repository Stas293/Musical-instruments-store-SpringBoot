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
                .route("auth-service-docs", r -> r.path("/docs/auth-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/auth-service/v3/api-docs", "/v3/api-docs"))
                        .uri("lb://auth-service"))
                .route("history-order-service-docs", r -> r.path("/docs/history-order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/history-order-service/v3/api-docs", "/v3/api-docs"))
                        .uri("lb://history-order-service"))
                .route("order-service-docs", r -> r.path("/docs/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/order-service/v3/api-docs", "/v3/api-docs"))
                        .uri("lb://order-service"))
                .route("instrument-service-docs", r -> r.path("/docs/instrument-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/instrument-service/v3/api-docs", "/v3/api-docs"))
                        .uri("lb://instrument-service"))
                .route("inventory-service-docs", r -> r.path("/docs/inventory-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/inventory-service/v3/api-docs", "/v3/api-docs"))
                        .uri("lb://inventory-service"))
                .build();
    }
}
