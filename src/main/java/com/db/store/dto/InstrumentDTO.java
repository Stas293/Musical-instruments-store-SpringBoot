package com.db.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.db.store.model.Instrument} entity
 */
public class InstrumentDTO implements Serializable {
    @Size(max = 255, min = 3, message = "validation.instrument.description.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.description.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    private String description;
    @Size(max = 255, min = 3, message = "validation.instrument.title.size")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "validation.instrument.title.pattern")
    @NotBlank(message = "validation.text.error.required.field")
    private String title;
    @NotNull(message = "validation.instrument.status.notNull")
    private StatusDTO status;
    @NotNull(message = "validation.instrument.price.notNull")
    private BigDecimal price;

    public InstrumentDTO() {
    }

    public InstrumentDTO(String description, String title, StatusDTO status, BigDecimal price) {
        this.description = description;
        this.title = title;
        this.status = status;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentDTO entity = (InstrumentDTO) o;
        return Objects.equals(this.description, entity.description) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.price, entity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, title, status, price);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "description = " + description + ", " +
                "title = " + title + ", " +
                "status = " + status + ", " +
                "price = " + price + ")";
    }
}