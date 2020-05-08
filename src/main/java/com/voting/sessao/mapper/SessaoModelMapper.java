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
        Date fechamento;

        if (request.getFechamento() != null) {
            fechamento = DateMapper.mapFrom(request.getFechamento());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, 1);
            fechamento = cal.getTime();
        }

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
