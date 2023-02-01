package com.db.store.constants;

public enum OrderConstants {
    ORDER_NOT_FOUND("order.not.found"),;

    private final String message;

    OrderConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
