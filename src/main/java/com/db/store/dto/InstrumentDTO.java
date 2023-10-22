package com.db.store.dto;

import com.db.store.model.Instrument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Instrument} entity
 */
@Value
@Builder
public class InstrumentDTO {
    @Size(max = 255, min = 3, message = "validation.instrument.description.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.description.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    String description;

    @Size(max = 255, min = 3, message = "validation.instrument.title.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.title.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    String title;

    @NotNull(message = "validation.instrument.status.notNull")
    StatusDTO status;

    @NotNull(message = "validation.instrument.price.notNull")
    BigDecimal price;
}