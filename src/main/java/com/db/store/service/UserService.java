package com.db.store.service;

import com.db.store.constants.RoleConstants;
import com.db.store.constants.UserConstants;
import com.db.store.exceptions.*;
import com.db.store.model.Role;
import com.db.store.model.User;
import com.db.store.repository.RoleRepository;
import com.db.store.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setDateCreated(LocalDateTime.now());
        user.setDateModified(LocalDateTime.now());
        Set<Role> role = Collections.singleton(
                roleRepository.findByCode(
                        UserConstants.DEFAULT_USER_ROLE.getMessage()).orElseThrow(
                        () -> new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()))
        );
        user.setRoles(role);
        return userRepository.save(user);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User disableUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public User enableUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public User updateUserRoles(Long id, List<Role> roleList) {
        Set<Role> roles = roleList.stream()
                .map(role -> roleRepository.findByCode(role.getCode())
                        .orElseThrow(
                                () -> new RoleNotFoundException(RoleConstants.ROLE_NOT_FOUND.getMessage()))
                )
                .collect(Collectors.toSet());
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User updateUserById(Long id, User user) {
        Optional<User> repUser = userRepository.findById(id);
        if (repUser.isPresent()) {
            User oldUser = repUser.get();
            checkUser(user, oldUser);
            updateUser(user, oldUser);
            return userRepository.save(oldUser);
        }
        throw new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage());
    }

    private void checkUser(User user, User oldUser) {
        Optional<User> userByLogin = getUserByLogin(user.getLogin());
        Optional<User> userByEmail = getUserByEmail(user.getEmail());
        Optional<User> userByPhone = getUserByPhone(user.getPhone());
        List<FieldError> errors = new ArrayList<>();
        if (userByLogin.isPresent() && !userByLogin.get().getId().equals(oldUser.getId())) {
            errors.add(new FieldError(
                    "user",
                    "login",
                    UserConstants.LOGIN_ALREADY_EXISTS.getMessage()));
        }
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(oldUser.getId())) {
            errors.add(new FieldError(
                    "user",
                    "email",
                    UserConstants.EMAIL_ALREADY_EXISTS.getMessage()));
        }
        if (userByPhone.isPresent() && !userByPhone.get().getId().equals(oldUser.getId())) {
            errors.add(new FieldError(
                    "user",
                    "phone",
                    UserConstants.PHONE_ALREADY_EXISTS.getMessage()));
        }
        if (!errors.isEmpty()) {
            throw new UserConflictException(errors);
        }
    }

    private void updateUser (User user, User oldUser) {
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setDateModified(LocalDateTime.now());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
    }

    @Transactional
    public User deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(UserConstants.USER_NOT_FOUND.getMessage()));
        List<FieldError> errors = new ArrayList<>();
        if (!user.getOrders().isEmpty()) {
            errors.add(new FieldError(
                    "user",
                    "orders",
                    UserConstants.USER_HAS_ORDERS.getMessage()));
            throw new UserConflictException(errors);
        }
        userRepository.delete(user);
        return user;
    }
}
