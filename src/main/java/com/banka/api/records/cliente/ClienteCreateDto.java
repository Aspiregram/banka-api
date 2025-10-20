package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ClienteCreateDto(
        UsuarioCreateDto usuCreateDto,

        @NotBlank(message = "Senha não deve ser nulo, vazio ou em branco")
        @Size(max = 50, message = "Documento não deve ultrapassar 65 caracteres em tamanho")
        String documento,

        @NotBlank(message = "País originário não deve ser nulo, vazio ou em branco")
        UUID paisOrigem,

        @NotBlank(message = "País atual não deve ser nulo, vazio ou em branco")
        UUID paisAtual,

        @NotBlank(message = "ONG não deve ser nula, vazia ou em branco")
        UUID ong
) {
}
