package com.voting.sessao.mapper;

import com.voting.pauta.model.PautaModel;
import com.voting.sessao.dao.SessaoEntity;
import com.voting.sessao.dto.SessaoDTO;
import com.voting.sessao.integration.SessaoRequest;
import com.voting.sessao.model.SessaoModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class SessaoModelMapper {
    public static SessaoModel mapFrom(SessaoEntity entity, PautaModel pauta) throws ParseException {
        return SessaoModel.builder()
            .id(entity.getId())
            .pauta(pauta)
            .abertura(DateMapper.mapFrom(entity.getAbertura()))
            .fechamento(DateMapper.mapFrom(entity.getFechamento()))
            .build();
    }

    public static SessaoModel mapFrom(SessaoRequest request, PautaModel pauta) throws ParseException {
        final Date fechamento = request.getFechamento() != null ?
            DateMapper.mapFrom(request.getFechamento()) :
            DateMapper.mapToFutureDate(new Date(), Calendar.MINUTE, 1);

        return SessaoModel.builder()
            .pauta(pauta)
            .abertura(new Date())
            .fechamento(fechamento)
            .build();
    }

    public static SessaoEntity mapToEntity(SessaoModel model) {
        return SessaoEntity.builder()
            .id(model.getId())
            .idPauta(model.getPauta().getId())
            .abertura(DateMapper.mapToString(model.getAbertura()))
            .fechamento(DateMapper.mapToString(model.getFechamento()))
            .build();
    }

    public static SessaoDTO mapToDTO(SessaoModel model) {
        return SessaoDTO.builder()
            .id(model.getId())
            .idPauta(model.getPauta().getId())
            .abertura(DateMapper.mapToString(model.getAbertura()))
            .fechamento(DateMapper.mapToString(model.getFechamento()))
            .build();
    }
}
