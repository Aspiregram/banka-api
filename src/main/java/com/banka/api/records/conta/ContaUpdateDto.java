package com.banka.api.records.conta;

import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record ContaUpdateDto(
        Long cliente,
        Long moeda,

        @Digits(integer = 15, fraction = 2,
                message = "Saldo não deve ultrapassar de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldo
) {
}
