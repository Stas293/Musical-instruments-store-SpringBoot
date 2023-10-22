package com.db.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
@ToString
public class InstrumentOrderId implements Serializable {
    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        InstrumentOrderId that = (InstrumentOrderId) o;
        return getInstrumentId() != null && Objects.equals(getInstrumentId(), that.getInstrumentId())
                && getOrderId() != null && Objects.equals(getOrderId(), that.getOrderId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(instrumentId, orderId);
    }
}