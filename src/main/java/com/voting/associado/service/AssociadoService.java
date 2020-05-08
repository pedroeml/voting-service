package com.voting.associado.service;

import com.voting.associado.dao.AssociadoDAO;
import com.voting.associado.dao.AssociadoEntity;
import com.voting.associado.integration.VoteStatusResponse;
import com.voting.associado.mapper.AssociadoModelMapper;
import com.voting.associado.model.AssociadoModel;
import com.voting.associado.model.VoteStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoDAO dao;

    @Autowired
    private AssociadoRestService restService;

    public AssociadoModel findById(long id) throws ResponseStatusException {
        final AssociadoEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Associado ID %d not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
        }

        return AssociadoModelMapper.mapFrom(entity);
    }

    public AssociadoModel findByIdWithStatus(long id) {
        final AssociadoModel model = this.findById(id);
        this.composeWithVoteStatus(model);

        return model;
    }

    public AssociadoModel find(AssociadoModel model) throws ResponseStatusException {
        AssociadoEntity entity = AssociadoModelMapper.mapToEntity(model);
        entity = this.dao.find(entity);

        if (entity == null) {
            final String reason = String.format("Associado CPF %s not found.", model.getCpf());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
        }

        return AssociadoModelMapper.mapFrom(entity);
    }

    public AssociadoModel findWithStatus(AssociadoModel model) {
        final AssociadoModel associado = this.find(model);
        this.composeWithVoteStatus(associado);

        return associado;
    }

    private void composeWithVoteStatus(AssociadoModel model) {
        final VoteStatusResponse response = this.restService.getVoteStatus(model.getCpf());
        final List<VoteStatusEnum> statusList = Arrays.asList(VoteStatusEnum.values());

        final VoteStatusEnum voteStatus = statusList.stream()
            .filter(status -> status.getStatus().equals(response.getStatus()))
            .findFirst()
            .orElseGet(() -> VoteStatusEnum.UNABLE);

        model.setVoteStatus(voteStatus);
    }

    public AssociadoModel add(AssociadoModel model) throws ResponseStatusException {
        if (!this.isCpfValid(model)) {
            final String reason = String.format("Associado CPF %s must have 11 digits and be valid.", model.getCpf());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        } else if (this.exists(model)) {
            final String reason = String.format("Associado CPF %s already exists.", model.getCpf());
            throw new ResponseStatusException(HttpStatus.CONFLICT, reason);
        }

        AssociadoEntity entity = AssociadoModelMapper.mapToEntity(model);
        entity = this.dao.save(entity);

        if (entity == null) {
            final String reason = "A error occured on creating creating new Pauta.";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
        }

        return AssociadoModelMapper.mapFrom(entity);
    }

    private boolean isCpfValid(AssociadoModel model) {
        boolean isValid;

        try {
            isValid = model.getCpf().length() == 11
                && Long.parseLong(model.getCpf()) > 0
                && this.restService.getVoteStatus(model.getCpf()) != null;
        } catch (Exception e) {
            isValid = false;
        }

        return isValid;
    }

    private boolean exists(AssociadoModel model) {
        AssociadoModel associado;

        try {
            associado = this.find(model);
        } catch (ResponseStatusException e) {
            associado = null;
        }

        return associado != null;
    }
}
