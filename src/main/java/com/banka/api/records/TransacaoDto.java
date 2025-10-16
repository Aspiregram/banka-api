package com.banka.api.records;

import com.banka.api.enums.Status;
import com.banka.api.enums.Tipo;
import com.banka.api.models.Conta;
import com.banka.api.models.Moeda;
import com.banka.api.models.Usuario;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDto(
        @NotBlank(message = "Conta originária é obrigatória")
        Conta contaOrigem,

        @NotBlank(message = "Conta final é obrigatória")
        Conta contaFinal,

        @NotBlank(message = "Valor originário é obrigatório")
        @Digits(integer = 15, fraction = 2)
        BigDecimal valorOriginal,

        @NotBlank(message = "Moeda originária é obrigatória")
        Moeda moedaOrigem,

        @NotBlank(message = "Valor final é obrigatório")
        @Digits(integer = 15, fraction = 2)
        BigDecimal valorFinal,

        @NotBlank(message = "Moeda final é obrigatória")
        Moeda moedaFinal,

        @NotBlank(message = "Taxa utilizada é obrigatória")
        BigDecimal taxaUtilizada,

        @NotBlank(message = "Tipo é obrigatório")
        Tipo tipo,

        @NotBlank(message = "Status é obrigatório")
        Status status
) {
}
