package com.db.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InstrumentOrderId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7042492300005014107L;
    @NotNull
    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public InstrumentOrderId(Long instrumentId, Long orderId) {
        this.instrumentId = instrumentId;
        this.orderId = orderId;
    }

    public InstrumentOrderId() {
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InstrumentOrderId entity = (InstrumentOrderId) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.instrumentId, entity.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, instrumentId);
    }

}