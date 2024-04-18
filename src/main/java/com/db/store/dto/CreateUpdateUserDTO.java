package com.db.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUpdateUserDTO(
    @Size(max = 255, min = 4, message = "validation.text.error.login.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\w+$", message = "validation.text.error.login.pattern")
    String login,

    @Size(max = 255, min = 4, message = "validation.text.error.first.name.size")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.text.error.first.name.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    String firstName,

    @Size(max = 255, min = 4, message = "validation.text.error.last.name.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "validation.text.error.last.name.pattern")
    String lastName,

    @Size(max = 255, min = 4, message = "validation.text.error.email.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "validation.text.error.email.pattern")
    String email,

    @Size(max = 13, min = 13, message = "validation.text.error.phone.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^\\+380[0-9]{9}$", message = "validation.text.error.phone.pattern")
    String phone,

    @Size(max = 255, min = 8, message = "validation.text.error.password.size")
    @NotBlank(message = "validation.text.error.required.field")
    String password
) {
}