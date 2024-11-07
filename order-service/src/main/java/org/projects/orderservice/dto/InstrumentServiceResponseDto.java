package org.projects.orderservice.dto;

import lombok.Builder;

@Builder
public record InstrumentServiceResponseDto(
        String id,

        String description,

        String title,

        Double price,

        Integer quantity
) {
}
