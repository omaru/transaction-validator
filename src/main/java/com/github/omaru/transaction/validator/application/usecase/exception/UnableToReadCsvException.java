package com.github.omaru.transaction.validator.application.usecase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnableToReadCsvException extends ResponseStatusException {

    public UnableToReadCsvException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
