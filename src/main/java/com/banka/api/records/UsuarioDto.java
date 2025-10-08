package com.banka.api.records;

import com.banka.api.enums.Role;
import com.banka.api.models.Ong;
import com.banka.api.models.Pais;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 30)
        String nome,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(max = 30)
        String sobrenome,

        @Email
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 30)
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(max = 50)
        String senha,

        @NotBlank(message = "Papel é obrigatório")
        @Size(max = 30)
        Role role,

        @NotBlank(message = "País é obrigatório")
        Pais pais,

        @NotBlank(message = "ONG é obrigatória")
        Ong ong
) {
}
