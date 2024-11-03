package org.projects.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/users/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("auth-service-circuit-breaker")
                                .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                .setRouteId("auth-service-circuit-breaker")
                        ))
                        .uri("lb://auth-service"))
                .route("history-order-service", r -> r.path("/api/order-history/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("history-order-service-circuit-breaker")
                                .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                .setRouteId("history-order-service-circuit-breaker")
                        ))
                        .uri("lb://history-order-service"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("order-service-circuit-breaker")
                                .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                .setRouteId("order-service-circuit-breaker")
                        ))
                        .uri("lb://order-service"))
                .route("instrument-service", r -> r.path("/api/instruments/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("instrument-service-circuit-breaker")
                                .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                .setRouteId("instrument-service-circuit-breaker")
                        ))
                        .uri("lb://instrument-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("inventory-service-circuit-breaker")
                                .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                .setRouteId("inventory-service-circuit-breaker")
                        ))
                        .uri("lb://inventory-service"))
                .route("eureka-service", r -> r.path("/eureka")
                        .filters(f -> f.rewritePath("/eureka", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-static", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .route("auth-service-docs", r -> r.path("/docs/auth-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/auth-service/v3/api-docs", "/v3/api-docs")
                                .circuitBreaker(c -> c.setName("auth-service-docs-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                        .setRouteId("auth-service-docs-circuit-breaker")
                                ))
                        .uri("lb://auth-service"))
                .route("history-order-service-docs", r -> r.path("/docs/history-order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/history-order-service/v3/api-docs", "/v3/api-docs")
                                .circuitBreaker(c -> c.setName("history-order-service-docs-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                        .setRouteId("history-order-service-docs-circuit-breaker")
                                ))
                        .uri("lb://history-order-service"))
                .route("order-service-docs", r -> r.path("/docs/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/order-service/v3/api-docs", "/v3/api-docs")
                                .circuitBreaker(c -> c.setName("order-service-docs-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                        .setRouteId("order-service-docs-circuit-breaker")
                                ))
                        .uri("lb://order-service"))
                .route("instrument-service-docs", r -> r.path("/docs/instrument-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/instrument-service/v3/api-docs", "/v3/api-docs")
                                .circuitBreaker(c -> c.setName("instrument-service-docs-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                        .setRouteId("instrument-service-docs-circuit-breaker")
                                ))
                        .uri("lb://instrument-service"))
                .route("inventory-service-docs", r -> r.path("/docs/inventory-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/docs/inventory-service/v3/api-docs", "/v3/api-docs")
                                .circuitBreaker(c -> c.setName("inventory-service-docs-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallbackRoute"))
                                        .setRouteId("inventory-service-docs-circuit-breaker")
                                ))
                        .uri("lb://inventory-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route()
                .GET("/fallbackRoute", request -> ServerResponse.status(503)
                        .bodyValue("Service Unavailable, please try again later"))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    WebFilter writeableHeaders() {
        return (exchange, chain) -> {
            HttpHeaders writeableHeaders = HttpHeaders.writableHttpHeaders(
                    exchange.getRequest().getHeaders());
            ServerHttpRequestDecorator writeableRequest = new ServerHttpRequestDecorator(
                    exchange.getRequest()) {
                @Override
                public HttpHeaders getHeaders() {
                    return writeableHeaders;
                }
            };
            ServerWebExchange writeableExchange = exchange.mutate()
                    .request(writeableRequest)
                    .build();
            return chain.filter(writeableExchange);
        };
    }
}
