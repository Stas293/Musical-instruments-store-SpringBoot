package org.projects.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * A DTO for the {@link org.projects.orderservice.model.Order} entity
 */
public record OrderCreationDto(
    @Size(max = 255, min = 3, message = "Title must be between 3 and 255 characters")
    @NotBlank(message = "Title is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only letters, numbers and spaces")
    String title,

    @Min(value = 1, message = "There must be at least one instrument in the order")
    List<InstrumentOrderCreateDto> instrumentOrders
) {
}
