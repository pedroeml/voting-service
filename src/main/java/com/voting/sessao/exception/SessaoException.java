package com.voting.sessao.exception;

import com.voting.exception.GenericDomainException;
import org.springframework.http.HttpStatus;

public class SessaoException extends GenericDomainException {

    public SessaoException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
