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
@Table(name = "transacoes")
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

    // Campo para armazenar a data da transação
    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime dataTransacao;

    // Campos de auditoria
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (dataTransacao == null) {
            dataTransacao = LocalDateTime.now(); // Define a data da transação como agora, se não for fornecida
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

}