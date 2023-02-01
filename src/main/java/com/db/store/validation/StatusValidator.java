package com.db.store.validation;

import com.db.store.model.Status;
import com.db.store.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StatusValidator implements Validator {
    private final StatusService statusService;

    @Autowired
    public StatusValidator(StatusService statusService) {
        this.statusService = statusService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Status.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Status status = (Status) target;
        if (statusService.getStatusByName(status.getName()) != null) {
            errors.rejectValue("name", "status.name", "validation.status.exists.name");
        }
        if (statusService.getStatusByCode(status.getCode()) != null) {
            errors.rejectValue("code", "status.code", "validation.status.exists.code");
        }
    }
}
