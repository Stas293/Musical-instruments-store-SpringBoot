package org.projects.apigateway.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtils {
    private final JwtConfig jwtConfig;

    public DecodedJWT validateToken(String token) {
        log.info("Validating token: {}", token);
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtConfig.secret()))
                .withJWTId("myJWT")
                .withIssuer("Spring Musical Store")
                .build();

        log.info("JWT Verifier: {}", jwtVerifier);
        return jwtVerifier.verify(token);
    }

    static String getBase64EncodedSecretKey(String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(DecodedJWT jwt) {
        Set<SimpleGrantedAuthority> grantedAuthoritySet = new HashSet<>();
        List<String> roles = jwt.getClaim("authorities").asList(String.class);
        log.info("Roles: {}", roles);
        roles.forEach(s -> grantedAuthoritySet.add(new SimpleGrantedAuthority(s)));
        return grantedAuthoritySet;
    }

    public Authentication getAuthentication(DecodedJWT jwt) {
        UserDetails userDetails = new User(
                jwt.getSubject(),
                "",
                getAuthorities(jwt)
        );
        log.info("User Details: {}", userDetails);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                getAuthorities(jwt));
    }

    public String getJwt(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(jwtConfig.header());
        log.info("JWT Header: {}", header);
        if (header != null && header.startsWith(jwtConfig.prefix())) {
            return header.substring(7);
        }
        return "no jwt";
    }

    public boolean checkJWTToken(String jwt) {
        return jwt != null
                && !jwt.isBlank()
                && jwt.split("\\.").length == 3
                && hasNoWhitespaces(jwt)
                && isNotExpired(jwt);
    }

    private boolean hasNoWhitespaces(String jwt) {
        return !jwt.contains(" ");
    }

    private boolean isNotExpired(String jwt) {
        return JWT.decode(jwt).getExpiresAt().after(new Date());
    }
}
