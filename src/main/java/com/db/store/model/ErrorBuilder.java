package com.db.store.model;

public interface ErrorBuilder {
    ErrorBuilderImpl code(String code);

    ErrorBuilderImpl message(String message);

    Error build();
}
