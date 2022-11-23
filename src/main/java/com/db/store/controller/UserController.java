package com.db.store.controller;

import com.db.store.dto.JwtRequestDto;
import com.db.store.dto.JwtResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto jwtRequestDto) {
        return ResponseEntity.ok(JwtResponseDto.builder().token(getJWTToken(jwtRequestDto)).build());
    }

    private String getJWTToken(JwtRequestDto jwtRequestDto) {
        String secretKey = "jwtSecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder()
                .setId("myJWT")
                .setSubject(jwtRequestDto.getLogin())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.RS512,
                        secretKey.getBytes()).compact();
        return "Bearer " + token;
    }
}
