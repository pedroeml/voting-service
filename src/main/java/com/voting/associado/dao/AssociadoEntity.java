package com.voting.associado.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ASSOCIADOS")
public class AssociadoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column(name = "CPF", nullable = false)
    private String cpf;
}
