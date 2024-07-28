package org.projects.historyorderservice.dto;

import java.math.BigDecimal;

public record InstrumentOrderDto(
        Long id,
        String instrumentId,
        Byte quantity,
        BigDecimal price
) {
}
