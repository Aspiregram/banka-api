package com.banka.api.records;

import com.banka.api.models.Pais;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MoedaDto(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 50)
        String nome,

        @NotBlank(message = "Sigla é obrigatória")
        @Size(max = 3)
        String sigla,

        @NotBlank(message = "Taxa de conversão é obrigatória")
        @Digits(integer = 10, fraction = 4)
        BigDecimal taxaConversao,

        @NotBlank(message = "País é obrigatório")
        Pais pais
) {
}
