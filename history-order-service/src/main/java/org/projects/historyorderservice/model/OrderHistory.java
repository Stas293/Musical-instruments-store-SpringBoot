package org.projects.historyorderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {
    @Id
    private String id;

    @Field("user_login")
    private String user;

    private BigDecimal totalSum;

    private String title;

    private Status status;
}