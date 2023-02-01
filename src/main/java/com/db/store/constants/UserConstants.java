package com.db.store.constants;

public enum UserConstants {
    DEFAULT_USER_ROLE("ROLE_USER"),
    USER_NOT_FOUND("user.not.found"),
    LOGIN_ALREADY_EXISTS("login.already.exists"),
    EMAIL_ALREADY_EXISTS("email.already.exists"),
    PHONE_ALREADY_EXISTS("phone.already.exists"), USER_HAS_ORDERS("user.has.orders");

    private final String message;

    UserConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
