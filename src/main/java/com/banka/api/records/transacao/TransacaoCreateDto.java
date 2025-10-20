package com.banka.api.records.transacao;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoCreateDto(
        @NotBlank(message = "Conta originária não deve ser nula, vazia ou em branco")
        UUID contaOrigem,

        @NotBlank(message = "Conta destinária não deve ser nula, vazia ou em branco")
        UUID contaDestino,

        @NotBlank(message = "Valor original não deve ser nulo, vazio ou em branco")
        @Digits(integer = 15, fraction = 2,
                message = "Valor original não deve ultrapassar" +
                        "de 15 unidades e 2 casas após a vírgula")
        BigDecimal valorOriginal,

        @NotBlank(message = "Moeda originária não deve ser nula, vazia ou em branco")
        UUID moedaOrigem,

        @NotBlank(message = "Valor convertido não deve ser nulo, vazio ou em branco")
        @Digits(integer = 15, fraction = 2,
                message = "Valor convertido não deve ultrapassar" +
                        "de 15 unidades e 2 casas após a vírgula")
        BigDecimal valorConvertido,

        @NotBlank(message = "Moeda destinária não deve ser nula, vazia ou em branco")
        UUID moedaDestino,

        @NotBlank(message = "Valor convertido não deve ser nulo, vazio ou em branco")
        @Digits(integer = 10, fraction = 4,
                message = "Valor convertido não deve ultrapassar" +
                        "de 10 unidades e 4 casas após a vírgula")
        BigDecimal taxaUtilizada,

        @NotBlank(message = "Tipo não deve ser nulo, vazio ou em branco")
        @Size(max = 19, message = "Tipo não deve ultrapassar 19 caracteres em tamanho")
        Tipo tipo,

        @NotBlank(message = "Status não deve ser nulo, vazio ou em branco")
        @Size(max = 17, message = "Status não deve ultrapassar 17 caracteres em tamanho")
        Status status,

        @NotBlank(message = "Data de transação não deve ser nulo, vazio ou em branco")
        LocalDateTime dataTransacao
) {
}
