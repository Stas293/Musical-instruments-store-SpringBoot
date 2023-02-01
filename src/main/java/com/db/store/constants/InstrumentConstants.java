package com.db.store.constants;

public enum InstrumentConstants {
    INSTRUMENT_NOT_FOUND("instrument.not.found"),
    INSTRUMENT_TITLE_EXISTS("instrument.title.exists"),
    INSTRUMENT_DESCRIPTION_EXISTS("instrument.description.exists"),
    INSTRUMENT_HAS_ORDERS("instrument.has.orders");

    private final String message;

    InstrumentConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
