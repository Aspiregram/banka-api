package com.banka.api.records.conta;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ContaCreateDto(
        @NotBlank(message = "Cliente não deve ser nulo, vazio ou em branco")
        Long cliente,

        @NotBlank(message = "Moeda não deve ser nula, vazia ou em branco")
        Long moeda,

        @Digits(integer = 15, fraction = 2,
                message = "Saldo não deve ultrapassar de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldo
) {
}
