package org.projects.orderservice.dto;

import java.math.BigDecimal;

public record InstrumentOrderDto(
        Long id,
        String instrumentId,
        Byte quantity,
        BigDecimal price
) {
}
