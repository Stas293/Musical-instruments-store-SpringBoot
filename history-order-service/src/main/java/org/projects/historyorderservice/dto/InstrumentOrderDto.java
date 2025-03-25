package org.projects.historyorderservice.dto;

public record InstrumentOrderDto(
        Long id,
        String instrumentId,
        Integer quantity,
        Double price
) {
}
