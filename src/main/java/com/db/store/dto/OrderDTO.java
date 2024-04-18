package com.db.store.dto;

import com.db.store.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link Order} entity
 */
@Value
public class OrderDTO implements Serializable {
    UserDTO user;

    @Size(max = 255, min = 3, message = "validation.order.title.size")
    @NotBlank(message = "validation.text.error.required.field")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.order.title.pattern")
    String title;

    StatusDTO status;

    List<InstrumentOrderDTO> instrumentOrders = new ArrayList<>();
}