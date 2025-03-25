package org.projects.orderservice.dto;

public record InstrumentOrderDto(
        Long id,
        String instrumentId,
        Integer quantity,
        Double price
) {
}
