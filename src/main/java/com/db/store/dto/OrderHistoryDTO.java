package com.db.store.dto;

import com.db.store.model.OrderHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/**
 * A DTO for the {@link OrderHistory} entity
 */
@Value
@Builder
public class OrderHistoryDTO {
    UserDTO user;

    @NotNull(message = "validation.order.totalSum.notNull")
    BigDecimal totalSum;

    @Size(max = 255, min = 5, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "validation.order.title.pattern")
    String title;

    @NotNull(message = "validation.order.status.notNull")
    StatusDTO status;
}