package com.db.store.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InstrumentConstants {
    INSTRUMENT_NOT_FOUND("instrument.not.found"),
    INSTRUMENT_TITLE_EXISTS("instrument.title.exists"),
    INSTRUMENT_DESCRIPTION_EXISTS("instrument.description.exists"),
    INSTRUMENT_HAS_ORDERS("instrument.has.orders"),
    INSTRUMENT_FIELD_EXISTS("instrument.%s.exists");

    private final String message;
}
