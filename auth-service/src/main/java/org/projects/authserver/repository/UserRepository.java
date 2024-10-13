package org.projects.authserver.repository;

import org.projects.authserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}
