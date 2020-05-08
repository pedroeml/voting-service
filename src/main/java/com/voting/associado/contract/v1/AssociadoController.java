package com.voting.associado.contract.v1;

import com.voting.associado.dto.AssociadoDTO;
import com.voting.associado.integration.AssociadoRequest;
import com.voting.associado.mapper.AssociadoModelMapper;
import com.voting.associado.model.AssociadoModel;
import com.voting.associado.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoService service;

    @GetMapping(value = "/{id}")
    public AssociadoDTO findById(@Valid @PathVariable long id) {
        final AssociadoModel model = this.service.findById(id);

        return AssociadoModelMapper.mapToDTO(model);
    }

    @GetMapping(value = "")
    public AssociadoDTO findByCpf(@RequestParam(name = "cpf") String cpf, @RequestParam(name = "status", defaultValue = "false") String status) {
        AssociadoModel model = AssociadoModel.builder()
            .cpf(cpf)
            .build();

        final boolean checkVoteStatus = Boolean.parseBoolean(status);
        model = checkVoteStatus ? this.service.findWithStatus(model) : this.service.find(model);

        final AssociadoDTO dto = AssociadoModelMapper.mapToDTO(model);

        if (checkVoteStatus) {
            dto.setAbleToVote(model.getVoteStatus().getCanVote());
        }

        return dto;
    }

    @PostMapping(value = "")
    public AssociadoDTO add(@RequestBody AssociadoRequest request) {
        AssociadoModel model = AssociadoModelMapper.mapFrom(request);
        model = this.service.add(model);

        return AssociadoModelMapper.mapToDTO(model);
    }
}
