package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "log_redefinicao_senha")
public class LogSenha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false, updatable = false)
    private Conta conta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id", nullable = false, updatable = false)
    private Ong ong;

    @Column(name = "data_redefinicao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
    private String motivo;
}
