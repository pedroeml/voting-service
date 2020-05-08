package com.voting.voto.mapper;

import com.voting.associado.model.AssociadoModel;
import com.voting.sessao.model.SessaoModel;
import com.voting.voto.dao.VotoEntity;
import com.voting.voto.dto.VotoDTO;
import com.voting.voto.integration.VotoRequest;
import com.voting.voto.model.VotoModel;

public class VotoModelMapper {
    public static VotoModel mapFrom(VotoEntity entity, SessaoModel sessao, AssociadoModel associado) {
        return VotoModel.builder()
            .id(entity.getId())
            .sessao(sessao)
            .associado(associado)
            .voto(entity.isVoto())
            .build();
    }

    public static VotoModel mapFrom(VotoRequest request, SessaoModel sessao, AssociadoModel associado) {
        return VotoModel.builder()
            .sessao(sessao)
            .associado(associado)
            .voto(request.getVoto())
            .build();
    }

    public static VotoEntity mapToEntity(VotoModel model) {
        return VotoEntity.builder()
            .id(model.getId())
            .idSessao(model.getSessao().getId())
            .idAssociado(model.getAssociado().getId())
            .voto(model.isVoto())
            .build();
    }

    public static VotoDTO mapToDTO(VotoModel model) {
        return VotoDTO.builder()
            .id(model.getId())
            .idSessao(model.getSessao().getId())
            .idAssociado(model.getAssociado().getId())
            .voto(model.isVoto())
            .build();
    }
}
