package org.projects.instrumentservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * A DTO for the {@link org.projects.instrumentservice.model.Instrument} entity
 */
@Builder
public record InstrumentResponseDto(
        String id,

        String description,

        String title,

        BigDecimal price,

        Integer quantity
) {
}
