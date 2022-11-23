package com.db.store.dto;

public class JwtResponseDtoBuilderImpl implements JwtResponseDtoBuilder {
    private String token;

    @Override
    public JwtResponseDtoBuilderImpl token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public JwtResponseDto build() {
        return new JwtResponseDto(token);
    }
}
