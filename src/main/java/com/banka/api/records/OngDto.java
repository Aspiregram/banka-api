package com.banka.api.records;

import com.banka.api.enums.Role;
import com.banka.api.models.Pais;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record OngDto(
        String id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120)
        String nome,

        @Email
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 100)
        String email,

        String telefone, // Novo campo

        String senha,

        @NotBlank(message = "Papel é obrigatório")
        Role role,

        Pais pais,

        BigDecimal saldoGlobal
) {
}