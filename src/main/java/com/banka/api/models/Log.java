package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_redefinicao_senha")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id", nullable = false, updatable = false)
    private Ong ong;

    @Column(name = "data_redefinicao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
    private String motivo;

}
