package com.banka.api.records;

import com.banka.api.models.Pais;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OngDto(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120)
        String nome,

        @Email
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha,

        @NotBlank(message = "Telefone é obrigatórip")
        @Size(max = 30)
        String telefone,

        @NotBlank(message = "País é obrigatório")
        Pais pais,

        @Digits(integer = 15, fraction = 2)
        BigDecimal saldoGlobal
) {
}
