package com.db.store.dto;

import com.db.store.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

/**
 * A DTO for the {@link Status} entity
 */
@Value
@Builder
public class StatusDTO {
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[A-Z_]+$", message = "validation.text.error.code.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.code.size")
    String code;

    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.text.error.name.pattern")
    @Size(max = 255, min = 3, message = "validation.text.error.name.size")
    String name;

    @NotNull(message = "validation.text.error.required.field")
    Boolean closed;
}