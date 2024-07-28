package org.projects.instrumentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    @Id
    private String id;

    private String title;

    private String description;

    private BigDecimal price;

    @Transient
    private Integer quantity;
}
