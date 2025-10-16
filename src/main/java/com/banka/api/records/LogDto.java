package com.banka.api.records;

import com.banka.api.models.Ong;
import com.banka.api.models.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record LogDto(
        @NotBlank(message = "Usuário é obrigatório")
        Usuario usuario,

        @NotBlank(message = "ONG é obrigatória")
        Ong ong,

        @NotBlank(message = "Data de alteração é obrigatória")
        LocalDateTime dataAlteracao,

        @NotBlank(message = "Motivo é obrigatório")
        String motivo
) {
}
