package com.db.store.controller;

import com.db.store.exceptions.*;
import com.db.store.model.error.Error;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class HandlerController {
    private final MessageSource messageSource;

    public HandlerController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<Error> handleStatusNotFoundException(StatusNotFoundException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Error> handleRoleNotFoundException(RoleNotFoundException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(StatusConflictException.class)
    public ResponseEntity<List<Error>> handleStatusConflict(StatusConflictException e, WebRequest request) {
        return getListErrors(request, e.getErrors());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<List<Error>> handleUserConflict(UserConflictException e, WebRequest request) {
        return getListErrors(request, e.getErrors());
    }

    private ResponseEntity<List<Error>> getListErrors(WebRequest request, List<FieldError> listErrors) {
        List<Error> errors = listErrors.stream()
                .map(error -> Error.builder()
                        .code(error.getField())
                        .message(messageSource.getMessage(
                                Objects.requireNonNull(error.getDefaultMessage()), null, request.getLocale()))
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(RoleConflictException.class)
    public ResponseEntity<List<Error>> handleRoleConflict(RoleConflictException e, WebRequest request) {
        return getListErrors(request, e.getErrors());
    }

    @ExceptionHandler(InstrumentNotFoundException.class)
    public ResponseEntity<Error> handleInstrumentNotFoundException(InstrumentNotFoundException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InstrumentConflictException.class)
    public ResponseEntity<List<Error>> handleInstrumentConflict(InstrumentConflictException e, WebRequest request) {
        return getListErrors(request, e.getErrors());
    }

    @ExceptionHandler(InstrumentReferenceException.class)
    public ResponseEntity<Error> handleInstrumentReferenceException(InstrumentReferenceException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.CONFLICT.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Error> handleOrderNotFoundException(OrderNotFoundException e, WebRequest request) {
        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(
                        messageSource.getMessage(e.getMessage(), null, request.getLocale()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> validationExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<Error> errors = fieldErrors.stream()
                .map(error -> Error.builder()
                        .code(error.getField())
                        .message(messageSource.getMessage(
                                Objects.requireNonNull(error.getDefaultMessage()), null, request.getLocale()))
                        .build())
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }
}
