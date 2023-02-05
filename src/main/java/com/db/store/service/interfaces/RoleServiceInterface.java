package com.db.store.service.interfaces;

import com.db.store.model.Role;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoleServiceInterface {
    Page<Role> getAllRoles(int page, int size);

    Role getRole(Long id);

    Role saveRole(Role role);

    Optional<Role> getRoleByName(String name);

    Optional<Role> getRoleByCode(String code);

    Role updateRoleById(Long id, Role role);

    Role deleteRoleById(Long id);
}
