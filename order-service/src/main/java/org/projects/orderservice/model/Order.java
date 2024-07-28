package org.projects.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order")
public class Order extends AuditingEntity<Long> {
    @Id
    private Long id;

    @Column("user_login")
    private String user;

    private String title;

    @Column("status_id")
    private Long statusId;

    @Transient
    private Status status;

    @Builder.Default
    private Boolean closed = false;

    @ToString.Exclude
    private List<InstrumentOrder> instrumentOrders = new ArrayList<>();
}