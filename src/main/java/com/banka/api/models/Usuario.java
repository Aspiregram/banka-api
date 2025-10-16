package com.banka.api.models;

import com.banka.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(length = 30, nullable = false)
    private String nome;

    @Column(length = 30, nullable = false)
    private String sobrenome;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String senha;

    @Column(length = 50)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_origem_id")
    private Pais paisOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_residencia_id")
    private Pais paisResidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    private Boolean ativo = true;

    @Column(name = "face_hash", length = 255)
    private String faceHash;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    @Transient
    public String getPassword() {
        return senha;
    }

    @Override
    @Transient
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return ativo;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}