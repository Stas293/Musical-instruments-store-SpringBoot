package org.projects.orderservice.dto;

public record InstrumentOrderDataResponseDto(
        Long id,
        String instrumentId,
        Integer quantity,
        Double price
) {
}
