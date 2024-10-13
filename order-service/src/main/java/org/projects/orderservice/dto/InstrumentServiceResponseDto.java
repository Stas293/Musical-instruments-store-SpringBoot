package org.projects.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record InstrumentServiceResponseDto(
        String id,

        String description,

        String title,

        BigDecimal price,

        Integer quantity
) {
}
