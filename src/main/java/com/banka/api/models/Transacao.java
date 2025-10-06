package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "DECIMAL(7,2)", nullable = false)
    private double valorOriginal;

    @OneToOne
    @JoinColumn(name = "moeda_origem_id", nullable = false)
    private Moeda moedaOrigem;

    @Column(columnDefinition = "DECIMAL(7,2)", nullable = false)
    private double valorFinal;

    @OneToOne
    @JoinColumn(name = "moeda_final_id", nullable = false)
    private Moeda moedaFinal;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataTransacao;

}
