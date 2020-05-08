package com.voting.sessao.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SESSOES")
public class SessaoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Setter
    @Column(name = "ID_PAUTA", nullable = false)
    private long idPauta;

    @Setter
    @Column(name = "ABERTURA", nullable = false)
    private String abertura;

    @Setter
    @Column(name = "FECHAMENTO", nullable = false)
    private String fechamento;
}
