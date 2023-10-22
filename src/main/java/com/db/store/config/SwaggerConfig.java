package com.db.store.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes(
        value = {
                @io.swagger.v3.oas.annotations.security.SecurityScheme(
                        name = "oauth2",
                        type = SecuritySchemeType.OAUTH2,
                        flows = @OAuthFlows(
                                authorizationCode = @OAuthFlow(
                                        authorizationUrl = "http://localhost:8080/oauth2/authorization/google",
                                        tokenUrl = "https://www.googleapis.com/oauth2/v4/token"
                                )
                        )
                ),
                @io.swagger.v3.oas.annotations.security.SecurityScheme(
                        name = "Bearer Authentication",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "JWT",
                        scheme = "bearer"
                )
        }
)
@SecurityRequirements(
        value = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "oauth2"),
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Bearer Authentication")
        }
)
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "My Contacts API",
                description = "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.",
                version = "1.0"
        ),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "oauth2"),
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Bearer Authentication")
        }
)
public class SwaggerConfig {
}
