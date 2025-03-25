package org.projects.orderservice.constants;

import lombok.Getter;

@Getter
public enum DefaultConstants {
    DEFAULT_STATUS("NEW_ORDER"),;

    private final String value;

    DefaultConstants(String value) {
        this.value = value;
    }

}
