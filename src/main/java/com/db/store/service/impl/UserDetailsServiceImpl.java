package com.db.store.service.impl;

import com.db.store.repository.UserRepository;
import com.db.store.security.UserPrincipal;
import com.db.store.utils.UserDetailsServiceExt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsServiceExt {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new UserPrincipal(
                userRepository.findByLogin(login)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User not found"))
        );
    }

    @Override
    public Optional<UserDetails> loadUserByUserEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new);
    }
}
