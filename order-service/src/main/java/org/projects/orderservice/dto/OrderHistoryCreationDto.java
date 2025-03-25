package org.projects.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderHistoryCreationDto(
    String user,

    List<InstrumentOrderDto> instrumentOrders,

    String title,

    String status
    ) {
}