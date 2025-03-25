package org.projects.authserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.projects.authserver.constants.RoleConstants;
import org.projects.authserver.constants.UserConstants;
import org.projects.authserver.dto.*;
import org.projects.authserver.exception.ResourceNotFoundException;
import org.projects.authserver.exception.ValidationException;
import org.projects.authserver.mapper.UserDtoMapper;
import org.projects.authserver.mapper.UserRegistrationDtoMapper;
import org.projects.authserver.model.Role;
import org.projects.authserver.model.User;
import org.projects.authserver.repository.RoleRepository;
import org.projects.authserver.repository.UserRepository;
import org.projects.authserver.security.jwt.JWTUtils;
import org.projects.authserver.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidation;
    private final JWTUtils jwtUtils;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseDTO login(JwtRequestDTO jwtRequestDto) {
        User user = userRepository.findByLogin(jwtRequestDto.login())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        if (!passwordEncoder.matches(jwtRequestDto.password(), user.getPassword())) {
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND.getMessage());
        }
        String jwt = jwtUtils.generateToken(
                jwtRequestDto.login(),
                user.getEmail(),
                user.getRoles());
        return new JwtResponseDTO(jwt);
    }

    @SneakyThrows
    @Transactional
    public UserDTO register(CreateUpdateUserDTO userDTO, BindingResult errors) {
        userValidation.validate(userDTO, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(objectMapper.writeValueAsString(errors.getAllErrors()));
        }
        return Optional.of(userDTO)
                .map(userRegistrationDtoMapper::toEntity)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setEnabled(true);
                    return user;
                })
                .map(this::setDefaultRole)
                .map(userRepository::save)
                .map(userDtoMapper::toDto)
                .get();
    }

    private User setDefaultRole(User user) {
        List<Role> role = Collections.singletonList(
                roleRepository.findByCode(
                        UserConstants.DEFAULT_USER_ROLE.getMessage()).orElseThrow(
                        () -> new ResourceNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()))
        );
        user.setRoles(role);
        return user;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO disableUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(false);
                    return user;
                })
                .map(userRepository::save)
                .map(userDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO enableUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(true);
                    return user;
                })
                .map(userRepository::save)
                .map(userDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUserRoles(Long id, List<RoleDTO> roles) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRoles(roleRepository.findAllByCodeIn(
                            roles.stream().map(RoleDTO::getCode).toList()));
                    return user;
                })
                .map(userRepository::save)
                .map(userDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public UserDTO updateUserData(CreateUpdateUserDTO updatedUserData, BindingResult errors, String login) {
        return userRepository.findByLogin(login)
                .map(user -> {
                    User newUser = userRegistrationDtoMapper.toEntity(updatedUserData);
                    checkUser(newUser, user, errors);
                    updateUser(newUser, user);
                    return userDtoMapper.toDto(userRepository.save(user));
                }).orElseThrow(
                        () -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    private void checkUser(User user, User oldUser, BindingResult errors) {
        Optional<User> userByLogin = getUserByLogin(user.getLogin());
        Optional<User> userByEmail = getUserByEmail(user.getEmail());
        Optional<User> userByPhone = getUserByPhone(user.getPhone());

        Optional<FieldError> loginError = checkExistingUser(userByLogin, oldUser, "login");
        Optional<FieldError> emailError = checkExistingUser(userByEmail, oldUser, "email");
        Optional<FieldError> phoneError = checkExistingUser(userByPhone, oldUser, "phone");

        loginError.ifPresent(errors::addError);
        emailError.ifPresent(errors::addError);
        phoneError.ifPresent(errors::addError);

        if (errors.hasErrors()) {
            throw new ValidationException(objectMapper.writeValueAsString(errors.getAllErrors()));
        }
    }

    private Optional<FieldError> checkExistingUser(Optional<User> userByLogin, User oldUser, String name) {
        if (userByLogin.isPresent() && !userByLogin.get().getId().equals(oldUser.getId())) {
            return Optional.of(
                    new FieldError(
                            "user",
                            name,
                            String.format(UserConstants.USER_FIELD_EXISTS.getMessage(), name)));
        }
        return Optional.empty();
    }

    private Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    private Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    private void updateUser(User user, User oldUser) {
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDTO> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(userDtoMapper::toDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
