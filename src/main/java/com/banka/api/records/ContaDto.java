package com.banka.api.records;

import com.banka.api.models.Moeda;
import com.banka.api.models.Usuario;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ContaDto(
        @NotBlank(message = "Usuário é obrigatório")
        Usuario usuario,

        @NotBlank(message = "Moeda é obrigatória")
        Moeda moeda,

        @Digits(integer = 15, fraction = 2)
        BigDecimal saldo
) {
}
