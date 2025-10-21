package com.banka.api.records.transacao;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDto(
        Long id,
        Long contaOrigem,
        Long contaDestino,
        BigDecimal valorOriginal,
        Long moedaOrigem,
        BigDecimal valorConvertido,
        Long moedaDestino,
        BigDecimal taxaUtilizada,
        Tipo tipo,
        Status status,
        LocalDateTime dataTransacao
) {
}
