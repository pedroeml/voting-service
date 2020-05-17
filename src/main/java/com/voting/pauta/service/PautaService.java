package com.voting.pauta.service;

import com.voting.pauta.dao.PautaDAO;
import com.voting.pauta.dao.PautaEntity;
import com.voting.pauta.exception.PautaException;
import com.voting.pauta.mapper.PautaModelMapper;
import com.voting.pauta.model.PautaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PautaService {
    private final Logger logger = LoggerFactory.getLogger(PautaService.class);

    @Autowired
    private PautaDAO dao;

    public PautaModel findById(long id) throws PautaException {
        final PautaEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Pauta ID %d not found.", id);
            throw new PautaException(reason, HttpStatus.NOT_FOUND);
        }

        return PautaModelMapper.mapFrom(entity);
    }

    public PautaModel add() throws PautaException {
        final PautaEntity entity = this.dao.save();

        if (entity == null) {
            throw new PautaException("An error occured on creating creating new Pauta.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        this.logger.info("A new Pauta was added: " + entity);

        return PautaModelMapper.mapFrom(entity);
    }
}
