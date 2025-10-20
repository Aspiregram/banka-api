package com.banka.api.records.conta;

import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaUpdateDto(
        UUID cliente,

        @Digits(integer = 15, fraction = 2,
                message = "Saldo não deve ultrapassar de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldo
) {
}
