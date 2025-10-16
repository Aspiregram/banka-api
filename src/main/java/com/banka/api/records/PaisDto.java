package com.banka.api.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaisDto(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotBlank(message = "Código ISO é obrigatório")
        @Size(max = 3)
        String isoCode
) {
}
