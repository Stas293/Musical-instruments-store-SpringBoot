package org.projects.orderservice.config;

import org.projects.orderservice.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtConfig.class})
public class ApplicationConfig {
}