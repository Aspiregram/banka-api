package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioResponseDto;

import java.util.UUID;

public record ClienteResponseDto(
        UsuarioResponseDto usuResponseDto,
        String documento,
        UUID paisOrigem,
        UUID paisAtual,
        UUID ong,
        Boolean estaAtivo
) {
}
