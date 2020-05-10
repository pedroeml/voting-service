package com.voting.pauta.mapper;

import com.voting.pauta.dto.ResultadoPautaDTO;
import com.voting.pauta.model.ResultadoPautaModel;

public class ResultadoPautaModelMapper {

    public static ResultadoPautaDTO mapToDTO(ResultadoPautaModel model) {
        return ResultadoPautaDTO.builder()
            .id(model.getId())
            .idSessao(model.getIdSessao())
            .isClosed(model.isClosed())
            .totalVotosPro(model.getTotalVotosPro())
            .totalVotosContra(model.getTotalVotosContra())
            .build();
    }
}
