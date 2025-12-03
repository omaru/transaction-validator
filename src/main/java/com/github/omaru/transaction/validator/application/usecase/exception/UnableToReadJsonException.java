package com.github.omaru.transaction.validator.application.usecase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnableToReadJsonException extends ResponseStatusException {

    public UnableToReadJsonException(Throwable t, String message) {
        super(HttpStatus.BAD_REQUEST, message, t);
    }
}
