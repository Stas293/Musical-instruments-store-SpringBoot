package com.db.store.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String code) {
        super(code);
    }

    public RoleNotFoundException(String code, Throwable cause) {
        super(code, cause);
    }

    public RoleNotFoundException(Throwable cause) {
        super(cause);
    }
}
