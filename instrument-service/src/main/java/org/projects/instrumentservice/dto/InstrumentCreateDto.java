package org.projects.instrumentservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

/**
 * A DTO for the {@link org.projects.instrumentservice.model.Instrument} entity
 */
@Builder
public record InstrumentCreateDto (
    @Size(max = 255, min = 3, message = "Description must be between 3 and 255 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Description must contain only letters, numbers and spaces")
    @NotBlank(message = "Description is required")
    String description,

    @Size(max = 255, min = 3, message = "Title must be between 3 and 255 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only letters, numbers and spaces")
    @NotBlank(message = "Title is required")
    String title,

    @NotNull(message = "Price is required")
    BigDecimal price,

    @DefaultValue("0")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    Integer quantity
    ){
}
