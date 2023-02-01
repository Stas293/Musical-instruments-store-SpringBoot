package com.db.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "instrument_order")
public class InstrumentOrder {
    @EmbeddedId
    private InstrumentOrderId id;

    @MapsId("orderId")
    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    @Min(value = 0, message = "validation.instrumentOrder.price.min")
    @Column(name = "price", nullable = false, precision = 17, scale = 2)
    private BigDecimal price;

    @NotNull(message = "validation.instrumentOrder.quantity.notNull")
    @Min(value = 0, message = "validation.instrumentOrder.quantity.min")
    @Column(name = "quantity", nullable = false)
    private Byte quantity;

    @MapsId("instrumentId")
    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
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

    public InstrumentOrderId getId() {
        return id;
    }

    public void setId(InstrumentOrderId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}