package com.voting.voto.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOTOS")
public class VotoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column(name = "ID_SESSAO", nullable = false)
    private long idSessao;

    @Column(name = "ID_ASSOCIADO", nullable = false)
    private long idAssociado;

    @Column(name = "VOTO", nullable = false)
    private boolean voto;
}
