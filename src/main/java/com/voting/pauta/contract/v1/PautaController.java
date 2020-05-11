package com.voting.pauta.contract.v1;

import com.voting.exception.DomainExceptionHandler;
import com.voting.pauta.dto.PautaDTO;
import com.voting.pauta.exception.PautaException;
import com.voting.pauta.mapper.PautaModelMapper;
import com.voting.pauta.model.PautaModel;
import com.voting.pauta.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService service;

    @GetMapping(value = "/{id}")
    public PautaDTO findById(@Valid @PathVariable long id) {
        PautaModel model;

        try {
            model = this.service.findById(id);
        } catch (PautaException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return PautaModelMapper.mapToDTO(model);
    }

    @PostMapping(value = "")
    public PautaDTO add() {
        PautaModel model;

        try {
            model = this.service.add();
        } catch (PautaException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return PautaModelMapper.mapToDTO(model);
    }
}
