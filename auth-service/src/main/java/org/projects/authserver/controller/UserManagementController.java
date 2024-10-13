package org.projects.authserver.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.authserver.dto.CreateUpdateUserDTO;
import org.projects.authserver.dto.RoleDTO;
import org.projects.authserver.dto.UserDTO;
import org.projects.authserver.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserManagementController {
    private final UserService userService;

    @PatchMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO disableUser(@PathVariable Long id) {
        return userService.disableUser(id);
    }

    @PatchMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO enableUser(@PathVariable Long id) {
        return userService.enableUser(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUserRoles(@PathVariable Long id,
                                   @RequestBody @Validated List<RoleDTO> roles) {
        return userService.updateUserRoles(id, roles);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@RequestBody @Validated CreateUpdateUserDTO updatedUserData,
                              BindingResult errors, @AuthenticationPrincipal UserDetails userDetails) {
        return userService.updateUserData(updatedUserData, errors, userDetails.getUsername());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "5") int size) {
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
