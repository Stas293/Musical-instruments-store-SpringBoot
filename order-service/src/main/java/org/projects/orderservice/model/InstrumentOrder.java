package org.projects.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "instrument_order")
public class InstrumentOrder {
    @Id
    private Long id;

    private Double price;

    private Integer quantity;

    private String instrumentId;
}