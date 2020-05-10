package com.voting.pauta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoPautaModel {
    private long id;
    private Long idSessao;
    private boolean isClosed;
    private long totalVotosPro;
    private long totalVotosContra;
}
