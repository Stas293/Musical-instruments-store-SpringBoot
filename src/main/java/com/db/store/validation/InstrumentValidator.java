package com.db.store.validation;

import com.db.store.model.Instrument;
import com.db.store.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InstrumentValidator implements Validator {
    private final InstrumentRepository instrumentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Instrument.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Instrument instrument = (Instrument) target;

        instrumentRepository.findByTitle(instrument.getTitle())
                .filter(instrument1 -> !Objects.equals(instrument1.getId(), instrument.getId()))
                .ifPresent(instrument1 -> errors.rejectValue(
                        "title",
                        "Instrument with this title already exists",
                        "validation.instrument.title.exists"));

        instrumentRepository.findByDescription(instrument.getDescription())
                .filter(instrument1 -> !Objects.equals(instrument1.getId(), instrument.getId()))
                .ifPresent(instrument1 -> errors.rejectValue(
                        "description",
                        "Instrument with this description already exists",
                        "validation.instrument.description.exists"));
    }
}
