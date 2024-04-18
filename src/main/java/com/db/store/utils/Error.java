package com.db.store.utils;

import lombok.Builder;

@Builder
public record Error(
        String code,
        String message
) {
}
