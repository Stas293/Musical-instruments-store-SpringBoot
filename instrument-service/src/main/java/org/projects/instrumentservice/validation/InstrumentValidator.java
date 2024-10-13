package org.projects.instrumentservice.validation;


import lombok.RequiredArgsConstructor;
import org.projects.instrumentservice.dto.InstrumentCreateDto;
import org.projects.instrumentservice.model.Instrument;
import org.projects.instrumentservice.repository.InstrumentRepository;
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
        InstrumentCreateDto instrument = (InstrumentCreateDto) target;

        instrumentRepository.findByTitle(instrument.title())
                .ifPresent(instrument1 -> errors.rejectValue(
                        "title",
                        "instrument.title.exists",
                        "Instrument with this title already exists"));
    }
}
