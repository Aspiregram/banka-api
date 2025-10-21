package com.banka.api.records.log;

import java.time.LocalDateTime;

public record LogSenhaResponseDto(
        Long id,
        Long conta,
        Long ong,
        LocalDateTime dataAlteracao,
        String motivo
) {
}
