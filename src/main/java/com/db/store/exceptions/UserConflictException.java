package com.db.store.exceptions;

import org.springframework.validation.FieldError;

import java.util.List;

public class UserConflictException extends RuntimeException {
    private final List<FieldError> errors;

    public UserConflictException(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "UserConflictException{" +
                "errors=" + errors +
                '}';
    }
}
