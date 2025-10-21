package com.banka.api.records.ong;

import com.banka.api.records.usuario.UsuarioResponseDto;

import java.math.BigDecimal;
import java.util.Set;

public record OngResponseDto(
        UsuarioResponseDto usuResponseDto,
        String telefone,
        Long pais,
        BigDecimal saldoGlobal,
        Set<Long> clientes
) {
}
