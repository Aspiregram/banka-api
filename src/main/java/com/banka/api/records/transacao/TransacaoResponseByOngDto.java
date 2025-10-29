package com.banka.api.records.transacao;

import com.banka.api.models.Conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseByOngDto(
        Long id,
        String contaOrigem,
        String contaDestino,
        BigDecimal valorOriginal,
        String paisOrigem,
        String moedaOrigem,
        BigDecimal valorConvertido,
        String paisDestino,
        String moedaDestino,
        BigDecimal taxaUtilizada,
        LocalDateTime dataTransacao
) {
}
