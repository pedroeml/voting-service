package com.voting.voto.model;

import com.voting.associado.model.AssociadoModel;
import com.voting.sessao.model.SessaoModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoModel {
    private long id;
    private SessaoModel sessao;
    private AssociadoModel associado;
    private boolean voto;
}
