package com.banka.api.records;

import com.banka.api.enums.Role;
import com.banka.api.models.Pais;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OngDto(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 50)
        String nome,

        @Email
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 30)
        String email,

        // Omitir senha do DTO para segurança
        String senha,

        @NotBlank(message = "Papel é obrigatório")
        @Size(max = 30)
        Role role,

        @NotBlank(message = "País é obrigatório")
        Pais pais
) {
}