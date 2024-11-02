package org.projects.orderservice.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final Environment environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String jwtToken = jwtUtils.getJwt(request);
            if (jwtUtils.checkJWTToken(jwtToken)) {
                if (checkIfInterServiceRequest(request, response, chain, jwtToken)) return;
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                if (decodedJWT.getClaim("authorities") != null) {
                    Authentication authentication = jwtUtils.getAuthentication(decodedJWT);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private boolean checkIfInterServiceRequest(HttpServletRequest request,
                                               HttpServletResponse response,
                                               FilterChain chain,
                                               String jwtToken) throws IOException, ServletException {
        String property = environment.getProperty("api.key.self");
        if (property != null) {
            if (jwtToken != null
                    && jwtToken.equals("%s%s".formatted(property, environment.getProperty("application.jwt.secret")))) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("inventory-service",
                                null,
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_SERVICE"))));
                chain.doFilter(request, response);
                return true;
            }
        }
        return false;
    }
}
