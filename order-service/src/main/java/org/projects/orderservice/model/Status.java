package org.projects.orderservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {
    @Id
    private Long id;

    private String code;

    private String name;

    private Boolean closed;

    @MappedCollection(idColumn = "status_id")
    @ToString.Exclude
    private List<NextStatus> nextStatuses = new ArrayList<>();
}
