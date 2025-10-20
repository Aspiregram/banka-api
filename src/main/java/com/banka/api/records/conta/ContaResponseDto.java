package com.banka.api.records.conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ContaResponseDto(
        UUID id,
        UUID cliente,
        Set<UUID> moeda,
        Set<UUID> transacoes,
        BigDecimal saldo,
        LocalDateTime criadaEm
) {
}
