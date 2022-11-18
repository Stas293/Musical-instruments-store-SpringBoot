package com.db.store.model;

public class Error {
    String code;
    String message;

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    public static class ErrorBuilder {
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
            return new Error(this);
        }
    }

    private Error(ErrorBuilder builder) {
        this.code = builder.code;
        this.message = builder.message;
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
