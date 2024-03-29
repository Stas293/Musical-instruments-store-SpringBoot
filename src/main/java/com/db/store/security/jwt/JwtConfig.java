package com.db.store.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.db.store.security.jwt.JWTUtils.getBase64EncodedSecretKey;


@ConfigurationProperties(prefix = "application.jwt")
public record JwtConfig(
        String secret,
        String header,
        String prefix,
        Integer expiration
) {
    @Override
    public String secret() {
        return getBase64EncodedSecretKey(secret);
    }

    @Override
    public String prefix() {
        return String.format("%s ", prefix);
    }
}
