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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ong")
public class Ong implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(length = 120, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 30)
    private String telefone;

    @Column(length = 255, nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @Column(name = "saldo_global", precision = 15, scale = 2)
    private BigDecimal saldoGlobal;

    @Column(length = 255)
    private String faceHash;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}