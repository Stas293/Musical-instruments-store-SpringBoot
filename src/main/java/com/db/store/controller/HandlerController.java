package com.db.store.controller;

import com.db.store.exceptions.StatusNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.db.store.model.Error;

@ControllerAdvice
public class HandlerController {

    @ExceptionHandler(StatusNotFoundException.class)
    protected ResponseEntity<Error> handleStatusNotFoundException(StatusNotFoundException e) {
        Error error = Error.builder()
                .code("404")
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }
}
