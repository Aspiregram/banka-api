package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moeda_id", nullable = false)
    private Moeda moeda;

    @Column(precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    public void onCreate() {
        if (saldo == null)
            saldo = BigDecimal.ZERO;

        criadaEm = LocalDateTime.now();
    }
}
