package com.voting.voto.contract.v1;

import com.voting.exception.DomainExceptionHandler;
import com.voting.exception.GenericDomainException;
import com.voting.voto.dto.VotoDTO;
import com.voting.voto.integration.VotoRequest;
import com.voting.voto.mapper.VotoModelMapper;
import com.voting.voto.model.VotoModel;
import com.voting.voto.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    @Autowired
    private VotoService service;

    @GetMapping(value = "/{id}")
    public VotoDTO findById(@Valid @PathVariable long id) {
        VotoModel model;

        try {
            model = this.service.findById(id);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return VotoModelMapper.mapToDTO(model);
    }

    @GetMapping(value = "")
    public List<VotoDTO> findAll(@RequestParam(name = "idSessao") long idSessao, @RequestParam(name = "idAssociado", required = false) Long idAssociado) {
        List<VotoModel> sessoes;

        try {
            sessoes = idAssociado != null ?
                this.service.findVotosBy(idSessao, idAssociado) :
                this.service.findVotosBy(idSessao);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return sessoes.stream()
            .map(VotoModelMapper::mapToDTO)
            .collect(Collectors.toList());
    }

    @PostMapping(value = "")
    public VotoDTO add(@RequestBody VotoRequest request) {
        VotoModel model;

        try {
            model = this.service.add(request);
        } catch (GenericDomainException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return VotoModelMapper.mapToDTO(model);
    }
}
