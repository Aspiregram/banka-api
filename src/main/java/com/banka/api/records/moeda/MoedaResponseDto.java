package com.banka.api.records.moeda;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record MoedaResponseDto(
        UUID id,
        String nome,
        String sigla,
        Set<UUID> transacoes,
        BigDecimal taxaConversao,
        UUID pais,
        UUID conta
) {
}
