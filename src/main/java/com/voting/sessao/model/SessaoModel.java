package com.voting.sessao.model;

import com.voting.pauta.model.PautaModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoModel {
    private long id;
    private PautaModel pauta;
    private Date abertura;
    private Date fechamento;
}
