package org.projects.historyorderservice.constants;

import lombok.Getter;
import org.projects.historyorderservice.model.Status;

@Getter
public enum DefaultStatuses {
    CANCELED(Status.builder()
            .name("Canceled")
            .description("Order was canceled")
            .build()),
    COMPLETE(Status.builder()
            .name("Complete")
            .description("Order was completed")
            .build()),
    ;

    private final Status status;

    DefaultStatuses(Status status) {
        this.status = status;
    }
}
