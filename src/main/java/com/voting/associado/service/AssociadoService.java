package com.voting.associado.service;

import com.voting.associado.dao.AssociadoDAO;
import com.voting.associado.dao.AssociadoEntity;
import com.voting.associado.exception.AssociadoException;
import com.voting.associado.integration.VoteStatusResponse;
import com.voting.associado.mapper.AssociadoModelMapper;
import com.voting.associado.model.AssociadoModel;
import com.voting.associado.model.VoteStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AssociadoService {
    private final Logger logger = LoggerFactory.getLogger(AssociadoService.class);

    @Autowired
    private AssociadoDAO dao;

    @Autowired
    private AssociadoRestService restService;

    public AssociadoModel findById(long id) throws AssociadoException {
        final AssociadoEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Associado ID %d not found.", id);
            throw new AssociadoException(reason, HttpStatus.NOT_FOUND);
        }

        return AssociadoModelMapper.mapFrom(entity);
    }

    public AssociadoModel findByIdWithStatus(long id) throws AssociadoException {
        final AssociadoModel model = this.findById(id);
        this.composeWithVoteStatus(model);

        return model;
    }

    public AssociadoModel find(AssociadoModel model) throws AssociadoException {
        AssociadoEntity entity = AssociadoModelMapper.mapToEntity(model);
        entity = this.dao.find(entity);

        if (entity == null) {
            final String reason = String.format("Associado CPF %s not found.", model.getCpf());
            throw new AssociadoException(reason, HttpStatus.NOT_FOUND);
        }

        return AssociadoModelMapper.mapFrom(entity);
    }

    public AssociadoModel findWithStatus(AssociadoModel model) throws AssociadoException {
        final AssociadoModel associado = this.find(model);
        this.composeWithVoteStatus(associado);

        return associado;
    }

    private void composeWithVoteStatus(AssociadoModel model) throws AssociadoException {
        final VoteStatusResponse response = this.restService.getVoteStatus(model.getCpf());
        final List<VoteStatusEnum> statusList = Arrays.asList(VoteStatusEnum.values());

        final VoteStatusEnum voteStatus = statusList.stream()
            .filter(status -> status.getStatus().equals(response.getStatus()))
            .findFirst()
            .orElseGet(() -> VoteStatusEnum.UNABLE);

        model.setVoteStatus(voteStatus);
    }

    public AssociadoModel add(AssociadoModel model) throws AssociadoException {
        if (!this.isCpfValid(model)) {
            final String reason = String.format("Associado CPF %s must have 11 digits and be valid.", model.getCpf());
            throw new AssociadoException(reason, HttpStatus.BAD_REQUEST);
        } else if (this.exists(model)) {
            final String reason = String.format("Associado CPF %s already exists.", model.getCpf());
            throw new AssociadoException(reason, HttpStatus.CONFLICT);
        }

        AssociadoEntity entity = AssociadoModelMapper.mapToEntity(model);
        entity = this.dao.save(entity);

        if (entity == null) {
            throw new AssociadoException("An error occured on creating creating new Associado.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        this.logger.info("A new Associado was added: " + entity);

        return AssociadoModelMapper.mapFrom(entity);
    }

    private boolean isCpfValid(AssociadoModel model) {
        boolean isValid;

        try {
            isValid = model.getCpf() != null
                && model.getCpf().length() == 11
                && Long.parseLong(model.getCpf()) > 0
                && this.restService.getVoteStatus(model.getCpf()) != null;
        } catch (Exception e) {
            this.logger.error("An exception was thrown on method isCpfValid.", e);
            isValid = false;
        }

        return isValid;
    }

    private boolean exists(AssociadoModel model) {
        AssociadoModel associado;

        try {
            associado = this.find(model);
        } catch (AssociadoException e) {
            associado = null;
        }

        return associado != null;
    }
}
