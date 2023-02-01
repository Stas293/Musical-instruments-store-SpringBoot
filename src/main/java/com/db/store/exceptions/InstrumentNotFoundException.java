package com.db.store.exceptions;

public class InstrumentNotFoundException extends RuntimeException {
    public InstrumentNotFoundException(String message) {
        super(message);
    }

    public InstrumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "InstrumentNotFoundException{" +
                "message=" + getMessage() +
                '}';
    }
}
