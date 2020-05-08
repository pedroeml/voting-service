package com.voting.sessao.service;

import com.voting.pauta.model.PautaModel;
import com.voting.pauta.service.PautaService;
import com.voting.sessao.dao.SessaoDAO;
import com.voting.sessao.dao.SessaoEntity;
import com.voting.sessao.integration.SessaoRequest;
import com.voting.sessao.mapper.DateMapper;
import com.voting.sessao.mapper.SessaoModelMapper;
import com.voting.sessao.model.SessaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessaoService {

    @Autowired
    private SessaoDAO dao;

    @Autowired
    private PautaService pautaService;

    public SessaoModel findById(long id) throws ResponseStatusException {
        final SessaoEntity entity = this.dao.get(id);

        if (entity == null) {
            final String reason = String.format("Sessao ID %d not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
        }

        final PautaModel pauta = this.pautaService.findById(entity.getIdPauta());
        return this.mapFromEntity(entity, pauta);
    }

    public List<SessaoModel> findSessoesBy(long idPauta) throws ResponseStatusException {
        final PautaModel pauta = this.pautaService.findById(idPauta);
        final List<SessaoEntity> entities = this.dao.getSessoesBy(idPauta);

        return entities.stream()
            .map(entity -> this.mapFromEntity(entity, pauta))
            .collect(Collectors.toList());
    }

    private SessaoModel mapFromEntity(SessaoEntity entity, PautaModel pauta) throws ResponseStatusException {
        SessaoModel model;

        try {
            model = SessaoModelMapper.mapFrom(entity, pauta);
        } catch (ParseException e) {
            final String reason = String.format("Sessao ID %d failed on parsing dates. It must be on format '%s'.", entity.getId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
        }

        return model;
    }

    private SessaoModel mapFromRequest(SessaoRequest request, PautaModel pauta) throws ResponseStatusException {
        SessaoModel model;

        try {
            model = SessaoModelMapper.mapFrom(request, pauta);
        } catch (ParseException e) {
            final String reason = String.format("Sessao Request object failed on parsing date. It must be on format '%s'.", DateMapper.datePattern);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
        }

        return model;
    }

    public SessaoModel add(SessaoRequest request) throws ResponseStatusException {
        if (request.getIdPauta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pauta ID can't be null.");
        }

        final PautaModel pauta = this.pautaService.findById(request.getIdPauta());
        final List<SessaoModel> sessoes = this.findSessoesBy(request.getIdPauta());

        if (!sessoes.isEmpty()) {
            final String reason = String.format("Pauta ID %d can't have more than 1 Sessao.", request.getIdPauta());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        SessaoModel model = this.mapFromRequest(request, pauta);

        if (model.getFechamento().before(model.getAbertura())) {
            final String abertura = DateMapper.mapToString(model.getAbertura());
            final String fechamento = DateMapper.mapToString(model.getFechamento());
            final String reason = String.format("fechamento=%s can't be before than abertura=%s.", fechamento, abertura);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        SessaoEntity entity = SessaoModelMapper.mapToEntity(model);
        entity = this.dao.save(entity);

        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured on creating creating new Sessao.");
        }

        return this.mapFromEntity(entity, pauta);
    }
}
