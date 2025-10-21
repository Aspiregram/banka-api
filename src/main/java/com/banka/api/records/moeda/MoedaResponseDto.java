package com.banka.api.records.moeda;

import java.math.BigDecimal;

public record MoedaResponseDto(
        Long id,
        String nome,
        String sigla,
        BigDecimal taxaConversao,
        Long pais
) {
}
