package com.db.store.service.impl;

import com.db.store.constants.RoleConstants;
import com.db.store.constants.UserConstants;
import com.db.store.dto.*;
import com.db.store.exceptions.RoleNotFoundException;
import com.db.store.exceptions.UserConflictException;
import com.db.store.exceptions.UserNotFoundException;
import com.db.store.mapper.UserDtoMapper;
import com.db.store.mapper.UserRegistrationDtoMapper;
import com.db.store.model.Role;
import com.db.store.model.User;
import com.db.store.repository.RoleRepository;
import com.db.store.repository.UserRepository;
import com.db.store.security.jwt.JWTUtils;
import com.db.store.service.UserService;
import com.db.store.utils.ExceptionParser;
import com.db.store.validation.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.db.store.constants.UserConstants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserValidator userValidation;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final UserDtoMapper userDtoMapper;

    @Override
    public JwtResponseDTO login(JwtRequestDTO jwtRequestDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtRequestDto.login(), jwtRequestDto.password());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDto.login());
        String jwt = jwtUtils.generateToken(
                jwtRequestDto.login(),
                userDetails.getAuthorities());
        return new JwtResponseDTO(jwt);
    }

    @Override
    @Transactional
    public UserDTO register(CreateUpdateUserDTO userDTO, BindingResult errors) {
        userValidation.validate(userDTO, errors);
        if (errors.hasErrors()) {
            ExceptionParser.parseValidation(errors);
        }
        User user = userRegistrationDtoMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        List<Role> role = Collections.singletonList(
                roleRepository.findByCode(
                        UserConstants.DEFAULT_USER_ROLE.getMessage()).orElseThrow(
                        () -> new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()))
        );
        user.setRoles(role);
        return userDtoMapper.toDto(userRepository.save(user));
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    @Transactional
    public UserDTO disableUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(false);
                    return userDtoMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(
                        () -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public UserDTO enableUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(true);
                    return userDtoMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(
                        () -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public UserDTO updateUserRoles(Long id, List<RoleDTO> roleList) {
        List<Role> roles = roleList.stream()
                .map(role -> roleRepository.findByCode(role.getCode())
                        .orElseThrow(
                                () -> new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()))
                )
                .toList();
        return userRepository.findById(id)
                .map(user -> {
                    user.setRoles(roles);
                    return userDtoMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(
                        () -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public UserDTO updateUserData(CreateUpdateUserDTO updatedUserData, BindingResult errors) {
        return userRepository.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).map(user -> {
            User newUser = userRegistrationDtoMapper.toEntity(updatedUserData);
            checkUser(newUser, user, errors);
            updateUser(newUser, user);
            return userDtoMapper.toDto(userRepository.save(user));
        }).orElseThrow(
                () -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
    }

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
            ExceptionParser.parseValidation(errors);
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

    private void updateUser(User user, User oldUser) {
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(userDtoMapper::toDto);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::toDto)
                .orElseThrow(ExceptionParser.exceptionSupplier(UserNotFoundException.class, USER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .map(user1 -> {
                    if (!user1.getOrders().isEmpty()) {
                        throw new UserConflictException(Collections.singletonList(
                                new FieldError(
                                        "user",
                                        "orders",
                                        UserConstants.USER_HAS_ORDERS.getMessage())));
                    }
                    return user1;
                })
                .map(user -> {
                    userRepository.delete(user);
                    return user;
                })
                .ifPresentOrElse(
                        user -> log.info("User with id {} was deleted", user.getId()),
                        () -> ExceptionParser.exceptionSupplier(
                                UserNotFoundException.class,
                                USER_NOT_FOUND.getMessage())
                );
    }
}
