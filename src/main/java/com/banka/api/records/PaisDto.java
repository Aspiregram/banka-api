package com.banka.api.records;

import com.banka.api.models.Moeda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PaisDto(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 50)
        String nome,

        @NotBlank(message = "Código ISO é obrigatório")
        @Size(max = 2)
        String isoCode,

        List<Moeda> moedas
) {
}