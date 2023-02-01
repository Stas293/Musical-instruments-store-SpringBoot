package com.db.store.exceptions;

import org.springframework.validation.FieldError;

import java.util.List;

public class StatusConflictException extends RuntimeException {
    private final List<FieldError> errors;
    public StatusConflictException(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        return "Status conflict";
    }
}
