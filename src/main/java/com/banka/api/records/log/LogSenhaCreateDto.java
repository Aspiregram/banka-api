package com.banka.api.records.log;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogSenhaCreateDto(
        @NotBlank(message = "Conta não deve ser nula, vazia ou em branco")
        UUID conta,

        @NotBlank(message = "ONG não deve ser nula, vazia ou em branco")
        UUID ong,

        @NotBlank(message = "Data de alteração não deve ser nula, vazia ou em branco")
        LocalDateTime dataAlteracao,

        @NotBlank(message = "Motivo não deve ser nulo, vazio ou em branco")
        String motivo
) {
}
