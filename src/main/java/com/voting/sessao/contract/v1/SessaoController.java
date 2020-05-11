package com.voting.sessao.contract.v1;

import com.voting.exception.DomainExceptionHandler;
import com.voting.exception.GenericDomainException;
import com.voting.sessao.dto.SessaoDTO;
import com.voting.sessao.integration.SessaoRequest;
import com.voting.sessao.mapper.SessaoModelMapper;
import com.voting.sessao.model.SessaoModel;
import com.voting.sessao.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService service;

    @GetMapping(value = "/{id}")
    public SessaoDTO findById(@Valid @PathVariable long id) {
        SessaoModel model;

        try {
            model = this.service.findById(id);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return SessaoModelMapper.mapToDTO(model);
    }

    @GetMapping(value = "")
    public List<SessaoDTO> findAll(@RequestParam(name = "idPauta") long idPauta) {
        List<SessaoModel> sessoes;

        try {
            sessoes = this.service.findSessoesBy(idPauta);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return sessoes.stream()
            .map(SessaoModelMapper::mapToDTO)
            .collect(Collectors.toList());
    }

    @PostMapping(value = "")
    public SessaoDTO add(@RequestBody SessaoRequest request) {
        SessaoModel model;

        try {
            model = this.service.add(request);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return SessaoModelMapper.mapToDTO(model);
    }
}
