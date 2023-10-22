package com.db.store.validation;

import com.db.store.model.Status;
import com.db.store.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StatusValidator implements Validator {
    private final StatusRepository statusRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Status.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Status status = (Status) target;

        statusRepository.findByName(status.getName())
                .ifPresent(status1 -> errors.rejectValue(
                        "name",
                        "Status with this name already exists",
                        "validation.status.name.exists"));

        statusRepository.findByCode(status.getCode())
                .ifPresent(status1 -> errors.rejectValue(
                        "code",
                        "Status with this code already exists",
                        "validation.status.code.exists"));
    }
}
