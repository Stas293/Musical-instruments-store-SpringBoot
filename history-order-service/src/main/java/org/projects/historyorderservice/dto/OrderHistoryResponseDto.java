package org.projects.historyorderservice.dto;

public record OrderHistoryResponseDto(
        String id,
        String user,
        String title,
        String status,
        Double totalSum
) {
}
