package com.banka.api.records.cliente;

import com.banka.api.records.usuario.UsuarioCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDto(
        UsuarioCreateDto usuCrtDto,

        @NotBlank(message = "Senha não deve ser nulo, vazio ou em branco")
        @Size(max = 50, message = "Documento não deve ultrapassar 50 caracteres em tamanho")
        String documento,

        @NotBlank(message = "País originário não deve ser nulo, vazio ou em branco")
        Long paisOrigem,

        @NotBlank(message = "País atual não deve ser nulo, vazio ou em branco")
        Long paisAtual,

        @NotBlank(message = "ONG não deve ser nula, vazia ou em branco")
        Long ong
) {
}
