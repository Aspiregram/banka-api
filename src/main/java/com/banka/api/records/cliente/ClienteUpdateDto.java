package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioUpdateDto;
import jakarta.validation.constraints.Size;

public record ClienteUpdateDto(
        UsuarioUpdateDto usuUpdateDto,

        @Size(max = 50, message = "Documento não deve ultrapassar 50 caracteres em tamanho")
        String documento,
        Long paisOrigem,
        Long paisAtual,
        Long ong
) {
}
