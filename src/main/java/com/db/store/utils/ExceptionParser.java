package com.db.store.utils;


import com.db.store.exceptions.ValidationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.function.Supplier;

@UtilityClass
@Slf4j
public class ExceptionParser {
    public void parseValidation(BindingResult bindingResult) {
        throw new ValidationException(bindingResult.getFieldErrors());
    }
    
    public <X extends RuntimeException> Supplier<? extends X> exceptionSupplier(X x)  {
        return () -> x;
    }

    @SuppressWarnings("unchecked")
    public <X extends RuntimeException> Supplier<X> exceptionSupplier(Class<? extends RuntimeException> exceptionClass, String message) {
        return () -> {
            try {
                return (X) exceptionClass.getConstructor(String.class).newInstance(message);
            } catch (Exception e) {
                log.error("Exception while creating exception", e);
                throw new RuntimeException(e);
            }
        };
    }
}
