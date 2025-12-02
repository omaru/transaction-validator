package com.github.omaru.transaction.validator.domain.model;

import lombok.Getter;

@Getter
public enum Reason {
    DUPLICATE_REFERENCE("Duplicate Transaction Reference"),
    INCORRECT_END_BALANCE("Incorrect End Balance");
    private final String description;

    Reason(String description) {
        this.description = description;
    }

}
