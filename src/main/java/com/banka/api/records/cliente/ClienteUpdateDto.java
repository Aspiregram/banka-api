package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioUpdateDto;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ClienteUpdateDto(
        UsuarioUpdateDto usuUpdateDto,

        @Size(max = 50, message = "Documento não deve ultrapassar 65 caracteres em tamanho")
        String documento,
        UUID paisOrigem,
        UUID paisAtual,
        UUID ong
) {
}
