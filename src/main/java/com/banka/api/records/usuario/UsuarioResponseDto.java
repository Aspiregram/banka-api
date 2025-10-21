package com.banka.api.records.usuario;

import com.banka.api.enums.Role;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String username,
        String nome,
        String sobrenome,
        String email,
        Role role,
        LocalDateTime criadoEm,
        LocalDateTime ultimoLogin
) {
}
