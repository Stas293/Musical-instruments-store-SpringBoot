package org.projects.apigateway.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.projects.apigateway.security.jwt.JWTUtils;
import org.projects.apigateway.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@EnableConfigurationProperties({JwtConfig.class})
public class SecurityConfig {
    private final JWTUtils jwtUtils;

    private final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs/**",
            "/webjars/**",
            "/docs/**"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/fallbackRoute").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/users/login", "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/instruments").permitAll()
                        .pathMatchers(SWAGGER_WHITELIST).permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(resourceServerCustomizer ->
                        resourceServerCustomizer.authenticationManagerResolver(reactiveAuthenticationManagerResolver()))
                .build();
    }
    private ReactiveAuthenticationManagerResolver<ServerWebExchange> reactiveAuthenticationManagerResolver() {
        return exchange -> Mono.just(authentication -> {
            String jwtToken = jwtUtils.getJwt(exchange.getRequest());
            if (jwtUtils.checkJWTToken(jwtToken)) {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                if (decodedJWT != null) {
                    return Mono.just(jwtUtils.getAuthentication(decodedJWT));
                }
            }
            return Mono.empty();
        });
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4200/**"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
