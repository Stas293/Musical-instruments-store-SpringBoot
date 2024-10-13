package org.projects.authserver.dto;

public record JwtRequestDTO (
        String login,
        String password
) {
}
