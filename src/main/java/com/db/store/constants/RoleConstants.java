package com.db.store.constants;

public enum RoleConstants {
    ROLE_NOT_FOUND("role.not.found"),
    ROLE_NAME_EXISTS("role.name.exists"),
    ROLE_CODE_EXISTS("role.code.exists");

    private final String message;

    RoleConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
