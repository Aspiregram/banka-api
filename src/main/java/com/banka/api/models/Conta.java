package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Moeda> moedas;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transacao> transacoes;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal saldo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    public void onCreate() {
        moedas = new HashSet<>();
        transacoes = new HashSet<>();
        saldo = BigDecimal.ZERO;
        criadaEm = LocalDateTime.now();
    }
}
