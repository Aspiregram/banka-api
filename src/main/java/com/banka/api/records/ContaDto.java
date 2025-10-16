package com.banka.api.records;

import java.math.BigDecimal;

public record ContaDto(
        String id,
        String usuarioId,
        String moedaId,
        BigDecimal saldo
) {
}