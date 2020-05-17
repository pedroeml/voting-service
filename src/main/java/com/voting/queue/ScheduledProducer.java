package com.voting.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.pauta.dto.ResultadoPautaDTO;
import com.voting.pauta.mapper.ResultadoPautaModelMapper;
import com.voting.pauta.model.ResultadoPautaModel;
import com.voting.pauta.service.ResultadoPautaService;
import com.voting.sessao.mapper.DateMapper;
import com.voting.sessao.model.SessaoModel;
import com.voting.sessao.service.SessaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledProducer {
    private final Logger logger = LoggerFactory.getLogger(ScheduledProducer.class);
    private List<SessaoModel> previewSessoes = Collections.emptyList();

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private ResultadoPautaService resultadoPautaService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;

    @Scheduled(fixedRate = 60000)
    public void computeVotingResults() {
        this.logger.info("Computing voting results...");
        final Date now = new Date();
        final Date oneMinuteLater = DateMapper.mapToFutureDate(new Date(), Calendar.MINUTE, 1);
        final List<ResultadoPautaModel> resultados = this.previewSessoes.stream()
            .map(sessao -> resultadoPautaService.findById(sessao.getPauta().getId()))
            .collect(Collectors.toList());

        resultados.forEach(this::send);

        this.previewSessoes = sessaoService.findSessoesBy(now, oneMinuteLater);
    }

    private void send(ResultadoPautaModel model) {
        final String routingKey = String.format("pauta.%d", model.getId());
        final ResultadoPautaDTO dto = ResultadoPautaModelMapper.mapToDTO(model);
        String message = String.format("Sending object %s to Exchange '%s' with Routing Key '%s'.", dto, exchange.getName(), routingKey);
        this.logger.info(message);

        try {
            final String serialized = new ObjectMapper().writeValueAsString(dto);
            this.rabbitTemplate.convertAndSend(exchange.getName(), routingKey, serialized);
        } catch (JsonProcessingException e) {
            this.logger.error("An exception was thrown on method send.", e);
        }
    }
}
