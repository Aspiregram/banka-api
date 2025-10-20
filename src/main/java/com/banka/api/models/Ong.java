package com.banka.api.models;

import com.banka.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ong extends Usuario {
    @Column(length = 30, nullable = false)
    private String telefone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Pais pais;

    @Column(precision = 15, scale = 2)
    private BigDecimal saldoGlobal;

    @OneToMany(mappedBy = "ong", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Cliente> clientes;

    @Override
    public void onCreate() {
        super.setRole(Role.ROLE_ONG);
        clientes = new HashSet<>();

        super.onCreate();
    }
}
