package com.voting.associado.mapper;

import com.voting.associado.dao.AssociadoEntity;
import com.voting.associado.dto.AssociadoDTO;
import com.voting.associado.integration.AssociadoRequest;
import com.voting.associado.model.AssociadoModel;

public class AssociadoModelMapper {

    public static AssociadoModel mapFrom(AssociadoEntity entity) {
        return AssociadoModel.builder()
            .id(entity.getId())
            .cpf(entity.getCpf())
            .build();
    }

    public static AssociadoModel mapFrom(AssociadoRequest request) {
        return AssociadoModel.builder()
            .cpf(request.getCpf())
            .build();
    }

    public static AssociadoEntity mapToEntity(AssociadoModel model) {
        return AssociadoEntity.builder()
            .id(model.getId())
            .cpf(model.getCpf())
            .build();
    }

    public static AssociadoDTO mapToDTO(AssociadoModel model) {
        return AssociadoDTO.builder()
            .id(model.getId())
            .cpf(model.getCpf())
            .build();
    }
}
