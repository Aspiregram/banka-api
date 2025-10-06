package com.banka.api.records;

import com.banka.api.models.Ong;
import com.banka.api.models.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record LogDto(
        Long id,

        @NotBlank(message = "Usuário é obrigatório")
        Usuario usuario,

        @NotBlank(message = "ONG é obrigatória")
        Ong ong,

        LocalDateTime dataAlteracao
) {
}
