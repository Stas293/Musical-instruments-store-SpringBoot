package org.projects.instrumentservice.config;

import org.projects.instrumentservice.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtConfig.class})
public class ApplicationConfig {
}