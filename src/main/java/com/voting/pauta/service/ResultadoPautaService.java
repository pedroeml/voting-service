package com.voting.pauta.service;

import com.voting.pauta.exception.PautaException;
import com.voting.pauta.model.ResultadoPautaModel;
import com.voting.sessao.model.SessaoModel;
import com.voting.sessao.service.SessaoService;
import com.voting.voto.model.VotoModel;
import com.voting.voto.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResultadoPautaService {

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private VotoService votoService;

    public ResultadoPautaModel findById(long id) throws PautaException {
        final List<SessaoModel> sessoes = this.sessaoService.findSessoesBy(id);
        final SessaoModel sessao = sessoes.stream()
            .findFirst()
            .orElseThrow(() -> {
                String reason = String.format("Pauta ID %d has no Sessao", id);
                return new PautaException(reason, HttpStatus.CONFLICT);
            });

        final List<VotoModel> votos = this.votoService.findVotosBy(sessao.getId());

        long totalVotosPro = votos.stream()
            .filter(VotoModel::isVoto)
            .count();

        return ResultadoPautaModel.builder()
            .id(sessao.getPauta().getId())
            .idSessao(sessao.getId())
            .isClosed(sessao.getFechamento().before(new Date()))
            .totalVotosPro(totalVotosPro)
            .totalVotosContra(votos.size() - totalVotosPro)
            .build();
    }
}
