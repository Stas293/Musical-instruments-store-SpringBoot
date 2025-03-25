package org.projects.historyorderservice.config;

import org.projects.historyorderservice.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtConfig.class})
public class ApplicationConfig {
}