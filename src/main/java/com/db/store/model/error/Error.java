package com.db.store.model.error;

public record Error(String code, String message) {
    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
