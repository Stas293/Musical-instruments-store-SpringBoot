package org.projects.instrumentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

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

//    @NotNull(message = "validation.instrument.status.notNull")
//    StatusDTO status,

    @NotNull(message = "Price is required")
    BigDecimal price
    ){
}
