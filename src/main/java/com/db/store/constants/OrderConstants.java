package com.db.store.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderConstants {
    ORDER_NOT_FOUND("order.not.found"),;

    private final String message;
}
