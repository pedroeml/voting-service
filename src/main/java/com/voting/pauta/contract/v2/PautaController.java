package com.voting.pauta.contract.v2;

import com.voting.exception.DomainExceptionHandler;
import com.voting.pauta.dto.ResultadoPautaDTO;
import com.voting.pauta.exception.PautaException;
import com.voting.pauta.mapper.ResultadoPautaModelMapper;
import com.voting.pauta.model.ResultadoPautaModel;
import com.voting.pauta.service.ResultadoPautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("PautaControllerV2")
@RequestMapping("/api/v2/pautas")
public class PautaController {

    @Autowired
    private ResultadoPautaService service;

    @GetMapping(value = "/{id}")
    public ResultadoPautaDTO findById(@Valid @PathVariable long id) {
        ResultadoPautaModel model;

        try {
            model = this.service.findById(id);
        } catch (PautaException e) {
            throw DomainExceptionHandler.handle(e);
        }

        return ResultadoPautaModelMapper.mapToDTO(model);
    }
}
