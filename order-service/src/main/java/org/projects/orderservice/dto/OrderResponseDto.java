package org.projects.orderservice.dto;

import java.util.List;

public record OrderResponseDto(
        Long id,
        String user,
        String title,
        String status,
        Boolean closed,
        List<InstrumentOrderDataResponseDto> instrumentOrders
) {
}
