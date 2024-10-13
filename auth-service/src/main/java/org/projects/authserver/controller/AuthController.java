package org.projects.authserver.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.authserver.dto.CreateUpdateUserDTO;
import org.projects.authserver.dto.JwtRequestDTO;
import org.projects.authserver.dto.JwtResponseDTO;
import org.projects.authserver.dto.UserDTO;
import org.projects.authserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class AuthController {
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
}
