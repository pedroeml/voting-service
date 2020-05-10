package com.voting.pauta.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadoPautaDTO {
    private long id;
    private Long idSessao;
    private boolean isClosed;
    private long totalVotosPro;
    private long totalVotosContra;
}
