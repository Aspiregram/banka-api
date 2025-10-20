package com.banka.api.records.pais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaisCreateDto(
        @NotBlank(message = "Nome não deve ser nulo, vazio ou em branco")
        @Size(max = 100, message = "Nome não deve ultrapassar 100 caracteres em tamanho")
        String nome,

        @NotBlank(message = "Código ISO não deve ser nulo, vazio ou em branco")
        @Size(max = 3, message = "Código ISO não deve ultrapassar 3 caracteres em tamanho")
        String isoCode
) {
}
