package com.db.store.exceptions;

public class InstrumentReferenceException extends RuntimeException {
    public InstrumentReferenceException(String message) {
        super(message);
    }

    public InstrumentReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentReferenceException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "InstrumentReferenceException{" +
                "message=" + getMessage() +
                '}';
    }
}
