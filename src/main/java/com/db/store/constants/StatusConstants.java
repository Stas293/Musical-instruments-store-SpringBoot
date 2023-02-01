package com.db.store.constants;

public enum StatusConstants {
    STATUS_NOT_FOUND("status.not.found"),
    STATUS_NAME_EXISTS("status.name.exists"),
    STATUS_CODE_EXISTS("status.code.exists"),
    ORDER_PROCESSING("ORDER_PROCESSING"),
    ORDER_ARRIVED("ARRIVED"),
    ORDER_CANCELED("CANCELED");

    private final String message;

    StatusConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
