package com.banka.api.records.usuario;

import com.banka.api.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDto(
        UUID id,
        String username,
        String name,
        String surname,
        String email,
        Role role,
        String faceHash,
        LocalDateTime criadoEm,
        LocalDateTime ultimoLogin
) {
}
