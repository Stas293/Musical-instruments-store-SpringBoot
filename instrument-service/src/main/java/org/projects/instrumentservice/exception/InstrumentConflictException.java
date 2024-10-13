package org.projects.instrumentservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class InstrumentConflictException extends RuntimeException {
    private final List<FieldError> errors;

    @Override
    public String toString() {
        return "InstrumentConflictException{errors=%s}".formatted(errors);
    }
}
