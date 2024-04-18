package com.db.store.validation;

import com.db.store.dto.RoleDTO;
import com.db.store.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleValidator implements Validator {
    private final RoleRepository roleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleDTO role = (RoleDTO) target;

        roleRepository.findByName(role.getName())
                .ifPresent(role1 -> errors.rejectValue(
                        "name",
                        "Role with this name already exists",
                        "validation.role.name.exists"));

        roleRepository.findByCode(role.getCode())
                .ifPresent(role1 -> errors.rejectValue(
                        "code",
                        "Role with this code already exists",
                        "validation.role.code.exists"));
    }
}
