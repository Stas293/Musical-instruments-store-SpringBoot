package com.db.store.exceptions;

public class StatusNotFoundException extends RuntimeException {

    public StatusNotFoundException(String message) {
        super(message);
    }

    public StatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "StatusNotFoundException{" +
                "message='" + super.getMessage() + '\'' +
                '}';
    }
}
