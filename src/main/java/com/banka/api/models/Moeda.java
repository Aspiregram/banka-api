package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moeda") // Adicionando o nome da tabela
public class Moeda {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id; // ID corrigido para String/UUID

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(columnDefinition = "CHAR(3)", nullable = false, unique = true)
    private String sigla;

    // CAMPO FALTANTE: Taxa de Conversão, essencial para o seu sistema de câmbio
    @Column(name = "taxa_conversao", precision = 10, scale = 4, nullable = false)
    private BigDecimal taxaConversao;

    // CORREÇÃO: Moeda pertence a um País (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id") // Nome da FK na tabela 'moeda'
    private Pais pais;

}