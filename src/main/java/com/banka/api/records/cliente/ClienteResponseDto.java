package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioResponseDto;

public record ClienteResponseDto(
        UsuarioResponseDto usuResponseDto,
        String documento,
        Long paisOrigem,
        Long paisAtual,
        Long ong,
        Boolean estaAtivo
) {
}
