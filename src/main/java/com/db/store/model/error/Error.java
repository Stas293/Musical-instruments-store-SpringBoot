package com.db.store.model.error;

public class Error {
    String code;
    String message;

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    public Error(String  code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
