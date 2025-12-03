package com.github.omaru.transaction.validator.interfaces.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoInputDataProvidedException extends ResponseStatusException {
    private static final String MESSAGE = "No input data provided. Please provide a file or record entries in the request body.";

    public NoInputDataProvidedException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
