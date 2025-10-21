package com.banka.api.records.log;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record LogSenhaCreateDto(
        @NotBlank(message = "Conta não deve ser nula, vazia ou em branco")
        Long conta,

        @NotBlank(message = "ONG não deve ser nula, vazia ou em branco")
        Long ong,

        @NotBlank(message = "Data de alteração não deve ser nula, vazia ou em branco")
        LocalDateTime dataAlteracao,

        @NotBlank(message = "Motivo não deve ser nulo, vazio ou em branco")
        String motivo
) {
}
