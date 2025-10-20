package com.banka.api.records.moeda;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record MoedaCreateDto(
        @NotBlank(message = "Nome não deve ser nulo, vazio ou em branco")
        @Size(max = 50, message = "Nome não deve ultrapassar 50 caracteres em tamanho")
        String nome,

        @NotBlank(message = "Sigla não deve ser nula, vazia ou em branco")
        @Size(max = 3, message = "Sigla não deve ultrapassar 3 caracteres em tamanho")
        String sigla,

        @NotBlank(message = "Taxa de conversão não deve ser nula, vazia ou em branco")
        @Digits(integer = 10, fraction = 4,
                message = "Taxa de conversão não deve ultrapassar" +
                        "de 10 unidades e 4 casas após a vírgula")
        BigDecimal taxaConversao,

        @NotBlank(message = "País não deve ser nulo, vazio ou em branco")
        UUID pais,
        UUID conta
) {
}
