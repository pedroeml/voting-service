package com.voting.voto.exception;

import com.voting.exception.GenericDomainException;
import org.springframework.http.HttpStatus;

public class VotoException extends GenericDomainException {

    public VotoException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
