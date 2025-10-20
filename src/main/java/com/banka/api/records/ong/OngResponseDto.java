package com.banka.api.records.ong;

import com.banka.api.records.usuario.UsuarioResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record OngResponseDto(
        UsuarioResponseDto usuResponseDto,
        String telefone,
        UUID pais,
        BigDecimal saldoGlobal,
        Set<UUID> clientes
) {
}
