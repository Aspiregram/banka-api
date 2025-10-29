package com.banka.api.models;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "log_transacao_realizada")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id", nullable = false, updatable = false)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id", nullable = false, updatable = false)
    private Conta contaDestino;

    @Column(precision = 15, scale = 2, nullable = false, updatable = false)
    private BigDecimal valorOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_origem_id", nullable = false, updatable = false)
    private Moeda moedaOrigem;

    @Column(precision = 15, scale = 2, nullable = false, updatable = false)
    private BigDecimal valorConvertido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_destino_id", nullable = false, updatable = false)
    private Moeda moedaDestino;

    @Column(precision = 10, scale = 4, nullable = false, updatable = false)
    private BigDecimal taxaUtilizada;

    @Enumerated(EnumType.STRING)
    @Column(length = 19, nullable = false, updatable = false)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(length = 17, nullable = false)
    private Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataTransacao;

    @PrePersist
    public void onCreate() {
        status = Status.PENDENTE;
        dataTransacao = LocalDateTime.now();
    }
}
