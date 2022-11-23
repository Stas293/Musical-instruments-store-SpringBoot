package com.db.store.dto;

public interface JwtResponseDtoBuilder {
    JwtResponseDtoBuilderImpl token(String token);

    JwtResponseDto build();
}
