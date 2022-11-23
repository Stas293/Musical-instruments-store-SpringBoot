package com.db.store.dto;

public class JwtResponseDto {
    private final String token;

    public static JwtResponseDtoBuilder builder() {
        return new JwtResponseDtoBuilderImpl();
    }

    JwtResponseDto(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JwtResponseDto{" +
                "token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtResponseDto that)) return false;

        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
