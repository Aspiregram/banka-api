package com.banka.api.records.conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponseDto(
        Long id,
        Long cliente,
        Long moeda,
        BigDecimal saldo,
        LocalDateTime criadaEm
) {
}
