package com.banka.api.models;

import com.banka.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente extends Usuario {
    @Column(length = 50, nullable = false)
    private String documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_origem_id", nullable = false, updatable = false)
    private Pais paisOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_atual_id", nullable = false)
    private Pais paisAtual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id", nullable = false)
    private Ong ong;

    @Column(nullable = false)
    private Boolean estaAtivo;

    @Override
    public void onCreate() {
        estaAtivo = true;

        if (super.getRole() == null)
            super.setRole(Role.ROLE_CLIENTE);

        super.onCreate();
    }
}
