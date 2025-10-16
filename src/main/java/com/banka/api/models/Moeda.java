package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(columnDefinition = "CHAR(3)", nullable = false, unique = true)
    private String sigla;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal taxaConversao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    @Column(nullable = false)
    private Pais pais;

}
