package com.banka.api.records.transacao;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponseDto(
        UUID id,
        UUID contaOrigem,
        UUID contaDestino,
        BigDecimal valorOriginal,
        UUID moedaOrigem,
        BigDecimal valorConvertido,
        UUID moedaDestino,
        BigDecimal taxaUtilizada,
        Tipo tipo,
        Status status,
        LocalDateTime dataTransacao
) {
}
