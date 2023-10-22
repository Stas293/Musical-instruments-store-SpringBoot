package com.db.store.service;

import com.db.store.dto.RoleDTO;
import com.db.store.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface RoleService {
    Page<RoleDTO> getAllRoles(int page, int size);

    RoleDTO getRole(Long id);

    RoleDTO saveRole(RoleDTO roleDTO, BindingResult bindingResult);

    Optional<Role> getRoleByName(String name);

    Optional<Role> getRoleByCode(String code);

    RoleDTO updateRoleById(Long id, RoleDTO role, BindingResult bindingResult);

    void deleteRoleById(Long id);
}
