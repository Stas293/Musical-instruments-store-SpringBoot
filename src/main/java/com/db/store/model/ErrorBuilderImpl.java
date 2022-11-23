package com.db.store.model;

public class ErrorBuilderImpl implements ErrorBuilder {
    private String code;
    private String message;

    @Override
    public ErrorBuilderImpl code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public ErrorBuilderImpl message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Error build() {
        return new Error(code, message);
    }
}
