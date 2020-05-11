package com.voting.pauta.exception;

import com.voting.exception.GenericDomainException;
import org.springframework.http.HttpStatus;

public class PautaException extends GenericDomainException {

    public PautaException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
