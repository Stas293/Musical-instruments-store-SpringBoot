package org.projects.historyorderservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class JWTUtils {
    private final JwtConfig jwtConfig;

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
        UserDetails userDetails = new User(
                jwt.getSubject(),
                "",
                getAuthorities(jwt)
        );
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                getAuthorities(jwt));
    }

    public boolean checkJWTToken(String jwt) {
        return jwt != null
                && !jwt.isBlank()
                && hasNoWhitespaces(jwt);
    }

    private boolean hasNoWhitespaces(String jwt) {
        return !jwt.contains(" ");
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
}
