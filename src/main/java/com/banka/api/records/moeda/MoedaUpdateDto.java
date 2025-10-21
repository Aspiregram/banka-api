package com.banka.api.records.moeda;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MoedaUpdateDto(
        @Size(max = 50, message = "Nome não deve ultrapassar 50 caracteres em tamanho")
        String nome,

        @Size(max = 3, message = "Sigla não deve ultrapassar 3 caracteres em tamanho")
        String sigla,

        @Digits(integer = 10, fraction = 4,
                message = "Taxa de conversão não deve ultrapassar" +
                        "de 10 unidades e 4 casas após a vírgula")
        BigDecimal taxaConversao,
        Long pais
) {
}
