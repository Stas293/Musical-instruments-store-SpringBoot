package com.db.store.validation;

import com.db.store.service.interfaces.InstrumentServiceInterface;
import com.db.store.model.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InstrumentValidator implements Validator {
    private final InstrumentServiceInterface instrumentService;

    @Autowired
    public InstrumentValidator(InstrumentServiceInterface instrumentService) {
        this.instrumentService = instrumentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Instrument.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Instrument instrument = (Instrument) target;

        if (instrumentService.getByTitle(instrument.getTitle()).isPresent()) {
            errors.rejectValue("title", "Instrument with this title already exists", "validation.instrument.title.exists");
        }

        if (instrumentService.getByDescription(instrument.getDescription()).isPresent()) {
            errors.rejectValue("description", "Instrument with this description already exists", "validation.instrument.description.exists");
        }
    }
}
