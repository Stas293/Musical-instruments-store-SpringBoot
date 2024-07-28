package org.projects.historyorderservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.projects.historyorderservice.model.OrderHistory;

import java.util.List;

/**
 * A DTO for the {@link OrderHistory} entity
 */
@Builder
public record OrderHistoryCreationDto (
    String user,

    @Min(value = 1, message = "There must be at least one instrument in the order")
    List<InstrumentOrderDto> instrumentOrders,

    @Size(max = 255, min = 5, message = "Title must be between 5 and 255 characters")
    @NotBlank(message = "Title is required")
    @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Title must contain only letters, numbers, underscores and spaces")
    String title,

    @NotBlank(message = "Status is required")
    String status
    ) {
}