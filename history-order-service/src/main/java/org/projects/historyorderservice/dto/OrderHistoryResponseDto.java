package org.projects.historyorderservice.dto;

import java.math.BigDecimal;

public record OrderHistoryResponseDto(
        String id,
        String user,
        String title,
        String status,
        BigDecimal totalSum
) {
}
