package com.db.store.dto;

import com.db.store.model.InstrumentOrder;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

/**
 * A DTO for the {@link InstrumentOrder} entity
 */
@Value
public class InstrumentOrderDTO {
    @NotNull(message = "validation.instrumentOrder.quantity.notNull")
    @Min(value = 0, message = "validation.instrumentOrder.quantity.min")
    Byte quantity;

    @Min(value = 0, message = "validation.instrumentOrder.price.min")
    @Column(name = "price", nullable = false, precision = 17, scale = 2)
    BigDecimal price;
    InstrumentDTO instrument;
}