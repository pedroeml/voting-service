package com.voting.queue;

import com.voting.pauta.model.ResultadoPautaModel;
import com.voting.pauta.service.ResultadoPautaService;
import com.voting.sessao.mapper.DateMapper;
import com.voting.sessao.model.SessaoModel;
import com.voting.sessao.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledTask {
    private List<SessaoModel> previewSessoes = Collections.emptyList();

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private ResultadoPautaService resultadoPautaService;

    @Scheduled(fixedRate = 60000)
    public void computeVotingResults() {
        final Date now = new Date();
        final Date oneMinuteLater = DateMapper.mapToFutureDate(new Date(), Calendar.MINUTE, 1);
        final List<ResultadoPautaModel> resultados = this.previewSessoes.stream()
            .map(sessao -> resultadoPautaService.findById(sessao.getPauta().getId()))
            .collect(Collectors.toList());

        resultados.forEach(resultado -> {
            System.out.println("Resultado Pauta" + resultado);
        });

        this.previewSessoes = sessaoService.findSessoesBy(now, oneMinuteLater);
    }
}
