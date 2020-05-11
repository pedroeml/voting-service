package com.voting.associado.exception;

import com.voting.exception.GenericDomainException;
import org.springframework.http.HttpStatus;

public class AssociadoException extends GenericDomainException {

    public AssociadoException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
