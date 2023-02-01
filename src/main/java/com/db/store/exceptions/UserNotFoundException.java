package com.db.store.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "UserNotFoundException{" +
                "message='" + super.getMessage() + '\'' +
                '}';
    }
}
