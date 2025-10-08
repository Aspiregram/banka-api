package com.banka.api.records;

import com.banka.api.models.Pais;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MoedaDto(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 30)
        String nome,

        @NotBlank(message = "Sigla é obrigatória")
        @Size(max = 3)
        String sigla,

        List<Pais> paises
) {
}