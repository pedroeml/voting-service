package com.voting.exception;

import org.springframework.web.server.ResponseStatusException;

public class DomainExceptionHandler {

    public static ResponseStatusException handle(GenericDomainException exception) {
        return new ResponseStatusException(exception.getStatus(), exception.getMessage());
    }
}
