package com.db.store.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusConstants {
    STATUS_NOT_FOUND("status.not.found"),
    STATUS_NAME_EXISTS("status.name.exists"),
    STATUS_CODE_EXISTS("status.code.exists"),
    ORDER_PROCESSING("ORDER_PROCESSING"),
    ORDER_ARRIVED("ARRIVED"),
    ORDER_CANCELED("CANCELED"),
    STATUS_FIELD_EXISTS("status.%s.exists");

    private final String message;
}
