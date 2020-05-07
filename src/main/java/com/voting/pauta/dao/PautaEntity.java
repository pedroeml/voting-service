package com.voting.pauta.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAUTAS")
public class PautaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;
}
