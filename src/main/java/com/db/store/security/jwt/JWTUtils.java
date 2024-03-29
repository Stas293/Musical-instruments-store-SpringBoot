package com.db.store.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.db.store.model.User;
import com.db.store.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JWTUtils {
    private final JwtConfig jwtConfig;

    public String generateToken(String login, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = getRolesList(authorities);

        String token = JWT.create()
                .withJWTId("myJWT")
                .withSubject(login)
                .withIssuer("Spring Musical Store")
                .withClaim("authorities", roles)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.expiration()))
                .sign(Algorithm.HMAC256(jwtConfig.secret()));
        return jwtConfig.prefix() + token;
    }

    private static List<String> getRolesList(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public DecodedJWT validateToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtConfig.secret()))
                .withJWTId("myJWT")
                .withIssuer("Spring Musical Store")
                .build();

        return jwtVerifier.verify(token);
    }

    static String getBase64EncodedSecretKey(String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(DecodedJWT jwt) {
        Set<SimpleGrantedAuthority> grantedAuthoritySet = new HashSet<>();
        List<String> roles = jwt.getClaim("authorities").asList(String.class);
        roles.forEach(s -> grantedAuthoritySet.add(new SimpleGrantedAuthority(s)));
        return grantedAuthoritySet;
    }

    public Authentication getAuthentication(DecodedJWT jwt) {
        UserDetails userDetails = new UserPrincipal(User.builder().login(getLogin(jwt)).build());
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                getAuthorities(jwt));
    }

    public String getLogin(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public String getJwt(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.header());
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
