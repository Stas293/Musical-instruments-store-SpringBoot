package org.projects.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "instrument_order")
public class InstrumentOrder {
    @Id
    private Long id;

    private BigDecimal price;

    private Byte quantity;

    private String instrumentId;
}