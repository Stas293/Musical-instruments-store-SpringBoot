package org.projects.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "next_status")
public class NextStatus {
    @Id
    private Long id;

    private Long statusId;

    private Long nextStatusId;
}