package org.projects.historyorderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    private Double totalSum;

    private String title;

    private String status;
}