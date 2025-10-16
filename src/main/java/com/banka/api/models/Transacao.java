package com.banka.api.models;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    @Column(nullable = false)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    @Column(nullable = false)
    private Conta contaDestino;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valorOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_origem_id", nullable = false)
    private Moeda moedaOrigem;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valorConvertido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_destino_id", nullable = false)
    private Moeda moedaDestino;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal taxaUtilizada;

    private LocalDateTime dataTransacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    @PrePersist
    protected void onCreate() {
        status = Status.STATUS_PENDENTE;
        dataCriacao = LocalDateTime.now();

        if (dataTransacao == null)
            dataTransacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

}
