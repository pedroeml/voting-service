package com.voting.pauta.service;

import com.voting.pauta.dao.PautaDAO;
import com.voting.pauta.dao.PautaEntity;
import com.voting.pauta.mapper.PautaModelMapper;
import com.voting.pauta.model.PautaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PautaService {

    @Autowired
    private PautaDAO dao;

    public PautaModel findById(long id) throws ResponseStatusException {
        final PautaEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Pauta ID %d not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
        }

        return PautaModelMapper.mapFrom(entity);
    }

    public PautaModel add() throws ResponseStatusException {
        final PautaEntity entity = this.dao.save();

        if (entity == null) {
            final String reason = "A error occured on creating creating new Pauta.";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
        }

        return PautaModelMapper.mapFrom(entity);
    }
}
