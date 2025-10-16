package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    @Column(name = "valor_original", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_origem_id", nullable = false)
    private Moeda moedaOrigem;

    @Column(name = "valor_convertido", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorConvertido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_destino_id", nullable = false)
    private Moeda moedaDestino;

    @Column(name = "taxa_utilizada", precision = 10, scale = 4)
    private BigDecimal taxaUtilizada;

    @Column(name = "data_transacao")
    private LocalDateTime dataTransacao;

    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    @Column(name = "status", length = 30)
    private String status;

    // Campos de auditoria
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();

        if (dataTransacao == null) {
            dataTransacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }
}