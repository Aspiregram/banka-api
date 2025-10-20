package com.banka.api.records.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDto(
        @Size(max = 65, message = "Username não deve ultrapassar 65 caracteres em tamanho")
        String username,

        @Size(max = 30, message = "Nome não deve ultrapassar 30 caracteres em tamanho")
        String nome,

        @Size(max = 30, message = "Sobrenome não deve ultrapassar 30 caracteres em tamanho")
        String sobrenome,

        @Size(max = 65, message = "Email não deve ultrapassar 65 caracteres em tamanho")
        @Email(message = "Email deve estar em um formato válido")
        String email,

        @Size(max = 65, message = "Senha não deve ultrapassar 65 caracteres em tamanho")
        String senha
) {
}
