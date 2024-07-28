package org.projects.orderservice.dto;

public record StatusResponseDto (
        Long id,
        String code,
        String name,
        Boolean closed
) {
}
