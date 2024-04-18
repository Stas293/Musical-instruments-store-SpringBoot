package com.db.store.config;


import com.db.store.security.jwt.JWTAuthorizationFilter;
import com.db.store.security.jwt.JwtAuthenticationEntryPoint;
import com.db.store.utils.UserDetailsServiceExt;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final UserDetailsServiceExt userService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("ROLE_USER");
        return authorityMapper;
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authManager(HttpSecurity http) {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setAuthoritiesMapper(authoritiesMapper());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers(HttpMethod.POST, "/auth", "/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/personal/**").hasAnyRole("USER", "SELLER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(config -> config
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())
                        )
                        .failureUrl("/swagger-ui")
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcIdToken idToken = userRequest.getIdToken();
                String email = idToken.getClaim("email");
                UserDetails userDetails = userService.loadUserByUserEmail(email)
                        .orElseThrow(() -> new OAuth2AuthenticationException("User not found"));
                DefaultOidcUser defaultOidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
                Set<Method> userMethods = Set.of(UserDetails.class.getMethods());
                return (OidcUser) Proxy.newProxyInstance(
                        SecurityConfiguration.class.getClassLoader(),
                        new Class[]{UserDetails.class, OidcUser.class},
                        (proxy, method, args) -> {
                            if (userMethods.contains(method)) {
                                return method.invoke(userDetails, args);
                            }
                            return method.invoke(defaultOidcUser, args);
                        });
            }
        };
    }
}
