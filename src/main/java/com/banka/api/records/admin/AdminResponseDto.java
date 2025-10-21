package com.banka.api.records.admin;

import com.banka.api.records.usuario.UsuarioResponseDto;

public record AdminResponseDto(
        UsuarioResponseDto usuResponseDto
) {
}
