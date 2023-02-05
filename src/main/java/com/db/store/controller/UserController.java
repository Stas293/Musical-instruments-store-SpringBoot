package com.db.store.controller;

import com.db.store.dto.*;
import com.db.store.model.Role;
import com.db.store.model.User;
import com.db.store.security.jwt.JWTUtils;
import com.db.store.service.interfaces.UserServiceInterface;
import com.db.store.utils.ObjectMapper;
import com.db.store.validation.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final UserServiceInterface userService;
    private final UserValidator userValidation;


    @Autowired
    public UserController(JWTUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          ObjectMapper objectMapper,
                          UserServiceInterface userService,
                          UserValidator userValidation) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.userValidation = userValidation;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO jwtRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtRequestDto.getLogin(), jwtRequestDto.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDto.getLogin());
        String jwt = jwtUtils.generateToken(
                jwtRequestDto.getLogin(),
                userDetails.getAuthorities());
        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO,
                                            BindingResult errors) throws MethodArgumentNotValidException {
        User user = objectMapper.map(registrationDTO, User.class);
        userValidation.validate(user, errors);
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getDeclaredMethods()[0], 0),
                    errors);
        }
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(objectMapper.map(registeredUser, UserDTO.class));
    }

    @PatchMapping("/admin/user/disable/{id}")
    public ResponseEntity<UserDTO> disableUser(@PathVariable Long id) {
        User user = userService.disableUser(id);
        return ResponseEntity.ok(objectMapper.map(user, UserDTO.class));
    }

    @PatchMapping("/admin/user/enable/{id}")
    public ResponseEntity<UserDTO> enableUser(@PathVariable Long id) {
        User user = userService.enableUser(id);
        return ResponseEntity.ok(objectMapper.map(user, UserDTO.class));
    }

    @PatchMapping("/admin/user/{id}/update")
    public ResponseEntity<UserDTO> updateUserRoles(@PathVariable Long id,
                                                   @RequestBody @Valid List<RoleDTO> roles) {
        List<Role> roleList = objectMapper.mapList(roles, Role.class);
        User user = userService.updateUserRoles(id, roleList);
        return ResponseEntity.ok(objectMapper.map(user, UserDTO.class));
    }

    @PutMapping("/personal/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid RegistrationDTO updatedUserData) {
        User user = objectMapper.map(updatedUserData, User.class);
        User updatedUser = userService.updateUserData(user);
        return ResponseEntity.ok(objectMapper.map(updatedUser, UserDTO.class));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "5") int size) {
        List<User> users = userService.getAllUsers(page, size).getContent();
        return ResponseEntity.ok(objectMapper.mapList(users, UserDTO.class));
    }

    @GetMapping("/admin/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(objectMapper.map(user, UserDTO.class));
    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        User user = userService.deleteUser(id);
        return ResponseEntity.ok(objectMapper.map(user, UserDTO.class));
    }
}
