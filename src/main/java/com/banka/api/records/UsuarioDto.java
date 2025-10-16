package com.banka.api.records;

import com.banka.api.models.Ong;
import com.banka.api.models.Pais;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 30)
        String nome,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(max = 30)
        String sobrenome,

        @Email
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha,

        @NotBlank(message = "Documento é obrigatório")
        @Size(max = 50)
        String documento,

        @NotBlank(message = "País originário é obrigatório")
        Pais paisOrigem,

        @NotBlank(message = "País residente é obrigatório")
        Pais paisResidencia,

        @NotBlank(message = "ONG é obrigatória")
        Ong ong
) {
}
