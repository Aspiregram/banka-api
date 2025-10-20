package com.banka.api.records.conta;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContaCreateDto(
        @NotBlank(message = "Cliente não deve ser nulo, vazio ou em branco")
        UUID cliente,

        @NotBlank(message = "Saldo não deve ser nulo, vazio ou em branco")
        @Digits(integer = 15, fraction = 2,
                message = "Saldo não deve ultrapassar de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldo
) {
}
