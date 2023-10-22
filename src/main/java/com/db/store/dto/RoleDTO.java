package com.db.store.dto;

import com.db.store.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

/**
 * A DTO for the {@link Role} entity
 */
@Value
@Builder
public class RoleDTO {
    @Size(max = 255, min = 3, message = "validation.role.code.size")
    @NotBlank(message = "validation.role.code.required")
    @Pattern(regexp = "^[A-Z_]+$", message = "validation.role.code.pattern")
    String code;

    @Size(max = 255, min = 3, message = "validation.role.name.size")
    @NotBlank(message = "validation.role.name.required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "validation.role.name.pattern")
    String name;
}