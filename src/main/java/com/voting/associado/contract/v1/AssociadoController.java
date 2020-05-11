package com.voting.associado.contract.v1;

import com.voting.associado.dto.AssociadoDTO;
import com.voting.associado.exception.AssociadoException;
import com.voting.associado.integration.AssociadoRequest;
import com.voting.associado.mapper.AssociadoModelMapper;
import com.voting.associado.model.AssociadoModel;
import com.voting.associado.service.AssociadoService;
import com.voting.exception.DomainExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoService service;

    @GetMapping(value = "/{id}")
    public AssociadoDTO findById(@Valid @PathVariable long id, @RequestParam(name = "status", required = false) boolean status) {
        AssociadoModel model;

        try {
            model = status ? this.service.findByIdWithStatus(id) : this.service.findById(id);
        } catch (AssociadoException e) {
            throw DomainExceptionHandler.handle(e);
        }

        final AssociadoDTO dto = AssociadoModelMapper.mapToDTO(model);

        if (status) {
            dto.setAbleToVote(model.getVoteStatus().getCanVote());
        }

        return dto;
    }

    @GetMapping(value = "")
    public AssociadoDTO findByCpf(@RequestParam(name = "cpf") String cpf, @RequestParam(name = "status", required = false) boolean status) {
        AssociadoModel model = AssociadoModel.builder()
            .cpf(cpf)
            .build();

        try {
            model = status ? this.service.findWithStatus(model) : this.service.find(model);
        } catch (AssociadoException e) {
            throw DomainExceptionHandler.handle(e);
        }

        final AssociadoDTO dto = AssociadoModelMapper.mapToDTO(model);

        if (status) {
            dto.setAbleToVote(model.getVoteStatus().getCanVote());
        }

        return dto;
    }

    @PostMapping(value = "")
    public AssociadoDTO add(@RequestBody AssociadoRequest request) {
        AssociadoModel model = AssociadoModelMapper.mapFrom(request);

        try {
            model = this.service.add(model);
        } catch (AssociadoException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return AssociadoModelMapper.mapToDTO(model);
    }
}
