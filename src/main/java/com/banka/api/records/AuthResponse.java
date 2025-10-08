package com.banka.api.records;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse(
        @NotBlank(message = "Token é obrigatório")
        String token
) {
}