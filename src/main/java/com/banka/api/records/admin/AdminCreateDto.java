package com.banka.api.records.admin;

import com.banka.api.records.usuario.UsuarioCreateDto;

public record AdminCreateDto(
        UsuarioCreateDto usuCreateDto
) {
}
