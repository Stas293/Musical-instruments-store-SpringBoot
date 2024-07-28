package org.projects.orderservice.dto;

import java.math.BigDecimal;

public record InstrumentOrderResponseDto(
        Long id,
        String instrumentId,
        Byte quantity,
        BigDecimal price
) {
}
