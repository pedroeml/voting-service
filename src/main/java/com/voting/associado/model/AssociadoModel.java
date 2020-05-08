package com.voting.associado.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssociadoModel {
    private long id;
    private String cpf;
    private VoteStatusEnum voteStatus;
}
