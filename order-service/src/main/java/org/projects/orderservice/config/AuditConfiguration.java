package org.projects.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class AuditConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("admin");
    }
}
