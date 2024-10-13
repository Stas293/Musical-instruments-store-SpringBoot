package org.projects.inventoryservice.config;

import org.projects.inventoryservice.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtConfig.class})
public class ApplicationConfig {
}