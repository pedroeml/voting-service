package com.voting.voto.service;

import com.voting.associado.model.AssociadoModel;
import com.voting.associado.service.AssociadoService;
import com.voting.sessao.model.SessaoModel;
import com.voting.sessao.service.SessaoService;
import com.voting.voto.dao.VotoDAO;
import com.voting.voto.dao.VotoEntity;
import com.voting.voto.integration.VotoRequest;
import com.voting.voto.mapper.VotoModelMapper;
import com.voting.voto.model.VotoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotoService {

    @Autowired
    private VotoDAO dao;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private AssociadoService associadoService;

    public VotoModel findById(long id) throws ResponseStatusException {
        final VotoEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Voto ID %d not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
        }

        final SessaoModel sessao = this.sessaoService.findById(entity.getIdSessao());
        final AssociadoModel associado = this.associadoService.findById(entity.getIdAssociado());
        return VotoModelMapper.mapFrom(entity, sessao, associado);
    }

    public List<VotoModel> findVotosBy(long idSessao) throws ResponseStatusException {
        final SessaoModel sessao = this.sessaoService.findById(idSessao);
        final List<VotoEntity> entities = this.dao.getVotosBy(idSessao);

        return entities.stream()
            .map(entity -> {
                final AssociadoModel associado = this.associadoService.findById(entity.getIdAssociado());
                return VotoModelMapper.mapFrom(entity, sessao, associado);
            })
            .collect(Collectors.toList());
    }

    public List<VotoModel> findVotosBy(long idSessao, long idAssociado) throws ResponseStatusException {
        final SessaoModel sessao = this.sessaoService.findById(idSessao);
        final AssociadoModel associado = this.associadoService.findById(idAssociado);
        final List<VotoEntity> entities = this.dao.getVotosBy(idSessao, idAssociado);

        return entities.stream()
            .map(entity -> VotoModelMapper.mapFrom(entity, sessao, associado))
            .collect(Collectors.toList());
    }

    public VotoModel add(VotoRequest request) throws ResponseStatusException {
        String reason = this.checkReasonToThrowException(request);
        if (reason != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        final SessaoModel sessao = this.sessaoService.findById(request.getIdSessao());
        final AssociadoModel associado = this.associadoService.findByIdWithStatus(request.getIdAssociado());
        final List<VotoModel> votos = this.findVotosBy(sessao.getId(), associado.getId());

        reason = this.checkReasonToThrowException(votos, sessao, associado);
        if (reason != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, reason);
        }

        VotoModel model = VotoModelMapper.mapFrom(request, sessao, associado);
        VotoEntity entity = VotoModelMapper.mapToEntity(model);
        entity = this.dao.save(entity);

        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured on creating creating new Voto.");
        }

        return VotoModelMapper.mapFrom(entity, sessao, associado);
    }

    private String checkReasonToThrowException(VotoRequest request) {
        String reason = null;

        if (request.getIdSessao() == null) {
            reason = "idSessao field can't be null.";
        } else if (request.getIdAssociado() == null) {
            reason = "idAssociado field can't be null.";
        } else if (request.getVoto() == null) {
            reason = "voto field can't be null.";
        }

        return reason;
    }

    private String checkReasonToThrowException(List<VotoModel> votos, SessaoModel sessao, AssociadoModel associado) {
        String reason = null;
        if (!votos.isEmpty()) {
            reason = String.format("Associado ID %d can't vote more than once on Sessao ID %d.", associado.getId(), sessao.getId());
        } else if (sessao.getAbertura().after(new Date())) {
            reason = String.format("Associado ID %d can't vote because Sessao ID %d is not open.", associado.getId(), sessao.getId());
        } else if (sessao.getFechamento().before(new Date())) {
            reason = String.format("Associado ID %d can't vote anymore because Sessao ID %d is closed.", associado.getId(), sessao.getId());
        } else if (!associado.getVoteStatus().getCanVote()) {
            reason = String.format("Associado ID %d is unable to vote.", associado.getId());
        }

        return reason;
    }
}
