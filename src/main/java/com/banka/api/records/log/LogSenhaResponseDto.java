package com.banka.api.records.log;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogSenhaResponseDto(
        UUID id,
        UUID conta,
        UUID ong,
        LocalDateTime dataAlteracao,
        String motivo
) {
}
