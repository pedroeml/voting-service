package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GenericDomainException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    public GenericDomainException(String reason, HttpStatus status) {
        super(reason);
        this.status = status;
    }
}
