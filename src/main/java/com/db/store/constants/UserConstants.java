package com.db.store.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserConstants {
    DEFAULT_USER_ROLE("ROLE_USER"),
    USER_NOT_FOUND("user.not.found"),
    LOGIN_ALREADY_EXISTS("login.already.exists"),
    EMAIL_ALREADY_EXISTS("email.already.exists"),
    PHONE_ALREADY_EXISTS("phone.already.exists"),
    USER_HAS_ORDERS("user.has.orders"),
    USER_FIELD_EXISTS("user.field.exists");

    private final String message;
}
