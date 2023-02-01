package com.db.store.model.error;

public class ErrorBuilder {
    private String code;
    private String message;

    public ErrorBuilder code(String code) {
        this.code = code;
        return this;
    }

    public ErrorBuilder message(String message) {
        this.message = message;
        return this;
    }

    public Error build() {
        return new Error(code, message);
    }
}
