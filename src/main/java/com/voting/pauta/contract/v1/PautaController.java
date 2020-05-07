package com.voting.pauta.contract.v1;

import com.voting.pauta.dto.PautaDTO;
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
        final PautaModel model = this.service.findById(id);

        return PautaModelMapper.mapToDTO(model);
    }

    @PostMapping(value = "")
    public PautaDTO add() {
        final PautaModel model = this.service.add();

        return PautaModelMapper.mapToDTO(model);
    }
}
