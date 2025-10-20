package com.banka.api.records.pais;

import jakarta.validation.constraints.Size;

public record PaisUpdateDto(
        @Size(max = 100, message = "Nome não deve ultrapassar 100 caracteres em tamanho")
        String nome,

        @Size(max = 3, message = "Código ISO não deve ultrapassar 3 caracteres em tamanho")
        String isoCode
) {
}
