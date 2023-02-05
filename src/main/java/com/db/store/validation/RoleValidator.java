package com.db.store.validation;

import com.db.store.model.Role;
import com.db.store.service.interfaces.RoleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RoleValidator implements Validator {
    private final RoleServiceInterface roleService;

    @Autowired
    public RoleValidator(RoleServiceInterface roleService) {
        this.roleService = roleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Role.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Role role = (Role) target;

        if (roleService.getRoleByName(role.getName()).isPresent()) {
            errors.rejectValue("name", "name", "Role with this name already exists");
        }

        if (roleService.getRoleByCode(role.getCode()).isPresent()) {
            errors.rejectValue("code", "code", "Role with this code already exists");
        }
    }
}
