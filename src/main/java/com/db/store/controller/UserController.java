package com.db.store.controller;

import com.db.store.dto.*;
import com.db.store.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDTO login(@RequestBody JwtRequestDTO jwtRequestDto) {
        return userService.login(jwtRequestDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@Validated @RequestBody CreateUpdateUserDTO createUpdateUserDTO,
                            BindingResult errors) {
        return userService.register(createUpdateUserDTO, errors);
    }

    @PatchMapping("/admin/user/{id}/disable")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO disableUser(@PathVariable Long id) {
        return userService.disableUser(id);
    }

    @PatchMapping("/admin/user/{id}/enable")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO enableUser(@PathVariable Long id) {
        return userService.enableUser(id);
    }

    @PatchMapping("/admin/user/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUserRoles(@PathVariable Long id,
                                   @RequestBody @Validated List<RoleDTO> roles) {
        return userService.updateUserRoles(id, roles);
    }

    @PutMapping("/personal/update")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@RequestBody @Validated CreateUpdateUserDTO updatedUserData,
                              BindingResult errors) {
        return userService.updateUserData(updatedUserData, errors);
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "5") int size) {
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/admin/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/admin/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
