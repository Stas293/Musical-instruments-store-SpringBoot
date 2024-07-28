package org.projects.historyorderservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public enum Status {
    CANCELED,
    COMPLETE,
}
