package com.db.store.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserDetailsServiceExt extends UserDetailsService {
    Optional<UserDetails> loadUserByUserEmail(String email);
}
