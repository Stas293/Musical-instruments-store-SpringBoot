package com.db.store.service.impl;

import com.db.store.constants.RoleConstants;
import com.db.store.dto.RoleDTO;
import com.db.store.exceptions.RoleNotFoundException;
import com.db.store.mapper.RoleDtoMapper;
import com.db.store.model.Role;
import com.db.store.repository.RoleRepository;
import com.db.store.service.RoleService;
import com.db.store.utils.ExceptionParser;
import com.db.store.validation.RoleValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Objects;
import java.util.Optional;

import static com.db.store.constants.RoleConstants.ROLE_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleDtoMapper roleDtoMapper;
    private final RoleValidator roleValidator;

    @Override
    public Page<RoleDTO> getAllRoles(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size))
                .map(roleDtoMapper::toDto);
    }

    @Override
    public RoleDTO getRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(ROLE_NOT_FOUND.getMessage()));
        return roleDtoMapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDTO saveRole(RoleDTO roleDTO, BindingResult bindingResult) {
        roleValidator.validate(roleDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
        Role role = roleDtoMapper.toEntity(roleDTO);
        return roleDtoMapper.toDto(roleRepository.save(role));
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Optional<Role> getRoleByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    @Transactional
    public RoleDTO updateRoleById(Long id, RoleDTO roleDTO, BindingResult bindingResult) {
        Role role = roleDtoMapper.toEntity(roleDTO);
        Optional<Role> repRole = roleRepository.findById(id);
        if (repRole.isPresent()) {
            Role oldRole = repRole.get();
            checkRole(role, oldRole, bindingResult);
            updateRole(role, oldRole);
            return roleDtoMapper.toDto(roleRepository.save(oldRole));
        }
        throw new RoleNotFoundException(ROLE_NOT_FOUND.getMessage());
    }

    private void checkRole(Role role, Role oldRole, BindingResult bindingResult) {
        Optional<Role> roleByName = getRoleByName(role.getName());
        Optional<Role> roleByCode = getRoleByCode(role.getCode());

        Optional<FieldError> nameError = checkExistingRole(roleByName, oldRole.getId(), "name");
        Optional<FieldError> codeError = checkExistingRole(roleByCode, oldRole.getId(), "code");

        nameError.ifPresent(bindingResult::addError);

        codeError.ifPresent(bindingResult::addError);

        if (bindingResult.hasErrors()) {
            ExceptionParser.parseValidation(bindingResult);
        }
    }

    private Optional<FieldError> checkExistingRole(
            Optional<Role> existingRole,
            Long oldRoleId,
            String fieldName) {

        if (existingRole.isPresent() && !Objects.equals(existingRole.get().getId(), oldRoleId)) {
            String errorMessage = String.format(
                    RoleConstants.ROLE_FIELD_EXISTS.getMessage(), fieldName);

            return Optional.of(new FieldError("role", fieldName, errorMessage));
        }

        return Optional.empty();
    }


    private void updateRole(Role role, Role oldRole) {
        oldRole.setName(role.getName());
        oldRole.setCode(role.getCode());
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        roleRepository.findById(id)
                .ifPresentOrElse(roleRepository::delete, () -> {
                    throw new RoleNotFoundException(ROLE_NOT_FOUND.getMessage());
                });
    }
}
