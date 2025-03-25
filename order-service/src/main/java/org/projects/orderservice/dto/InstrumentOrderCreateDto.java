package org.projects.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * A DTO for the {@link org.projects.orderservice.model.InstrumentOrder} entity
 */
@Builder
public record InstrumentOrderCreateDto(
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be greater than 0")
        Integer quantity,

        @NotNull(message = "Instrument ID is required")
        String instrumentId) {
}