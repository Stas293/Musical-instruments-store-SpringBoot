package com.db.store.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.db.store.model.InstrumentOrder} entity
 */
public class InstrumentOrderDTO implements Serializable {
    @NotNull(message = "validation.instrumentOrder.quantity.notNull")
    @Min(value = 0, message = "validation.instrumentOrder.quantity.min")
    private Byte quantity;

    @Min(value = 0, message = "validation.instrumentOrder.price.min")
    @Column(name = "price", nullable = false, precision = 17, scale = 2)
    private BigDecimal price;
    private InstrumentDTO instrument;

    public InstrumentOrderDTO() {
    }

    public InstrumentOrderDTO(Byte quantity, InstrumentDTO instrument) {
        this.quantity = quantity;
        this.instrument = instrument;
    }

    public Byte getQuantity() {
        return quantity;
    }

    public void setQuantity(Byte quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public InstrumentDTO getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentDTO instrument) {
        this.instrument = instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentOrderDTO that)) return false;

        if (!quantity.equals(that.quantity)) return false;
        if (!Objects.equals(price, that.price)) return false;
        return instrument.equals(that.instrument);
    }

    @Override
    public int hashCode() {
        int result = quantity.hashCode();
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + instrument.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InstrumentOrderDTO{" +
                "quantity=" + quantity +
                ", price=" + price +
                ", instrument=" + instrument +
                '}';
    }
}