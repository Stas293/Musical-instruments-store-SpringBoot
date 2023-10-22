package com.db.store.dto;

public record JwtRequestDTO (
        String login,
        String password
) {
}
