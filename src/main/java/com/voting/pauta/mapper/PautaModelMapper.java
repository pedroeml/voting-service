package com.voting.pauta.mapper;

import com.voting.pauta.dao.PautaEntity;
import com.voting.pauta.dto.PautaDTO;
import com.voting.pauta.model.PautaModel;

public class PautaModelMapper {

    public static PautaModel mapFrom(PautaEntity entity) {
        return PautaModel.builder()
            .id(entity.getId())
            .build();
    }

    public static PautaEntity mapToEntity(PautaModel model) {
        return PautaEntity.builder()
            .id(model.getId())
            .build();
    }

    public static PautaDTO mapToDTO(PautaModel model) {
        return PautaDTO.builder()
            .id(model.getId())
            .build();
    }
}
