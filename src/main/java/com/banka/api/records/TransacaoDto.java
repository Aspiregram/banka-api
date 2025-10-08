package com.banka.api.records;

import com.banka.api.models.Moeda;
import com.banka.api.models.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TransacaoDto(
        Long id,

        @NotBlank(message = "Valor originário é obrigatório")
        double valorOriginal,

        @NotBlank(message = "Moeda originária é obrigatória")
        Moeda moedaOrigem,

        @NotBlank(message = "Valor final é obrigatório")
        double valorFinal,

        @NotBlank(message = "Moeda final é obrigatória")
        Moeda moedaFinal,

        @NotBlank(message = "Usuário é obrigatório")
        Usuario usuario,

        LocalDateTime dataTransacao
) {
}