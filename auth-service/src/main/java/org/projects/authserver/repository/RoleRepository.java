package org.projects.authserver.repository;

import org.projects.authserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    List<Role> findAllByCodeIn(List<String> list);
}
