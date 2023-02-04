package com.db.store.service;

import com.db.store.constants.RoleConstants;
import com.db.store.exceptions.RoleConflictException;
import com.db.store.exceptions.RoleNotFoundException;
import com.db.store.model.Role;
import com.db.store.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Page<Role> getAllRoles(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size));
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()));
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Role> getRoleByCode(String code) {
        return roleRepository.findByCode(code);
    }

    public Role updateRoleById(Long id, Role role) {
        Optional<Role> repRole = roleRepository.findById(id);
        if (repRole.isPresent()) {
            Role oldRole = repRole.get();
            checkRole(role, oldRole);
            updateRole(role, oldRole);
            return roleRepository.save(oldRole);
        }
        throw new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage());
    }

    private void checkRole(Role role, Role oldRole) {
        Optional<Role> roleByName = getRoleByName(role.getName());
        Optional<Role> roleByCode = getRoleByCode(role.getCode());
        List<FieldError> errors = new ArrayList<>();
        if (roleByName.isPresent() && !roleByName.get().getId().equals(oldRole.getId())) {
            errors.add(
                    new FieldError(
                            "role",
                            "name",
                            RoleConstants.ROLE_NAME_EXISTS.getMessage()));
        }
        if (roleByCode.isPresent() && !roleByCode.get().getId().equals(oldRole.getId())) {
            errors.add(
                    new FieldError(
                            "role",
                            "code",
                            RoleConstants.ROLE_CODE_EXISTS.getMessage()));
        }
        if (!errors.isEmpty()) {
            throw new RoleConflictException(errors);
        }
    }

    private void updateRole(Role role, Role oldRole) {
        oldRole.setName(role.getName());
        oldRole.setCode(role.getCode());
    }

    public Role deleteRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            Role oldRole = role.get();
            roleRepository.delete(oldRole);
            return oldRole;
        }
        throw new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage());
    }
}
