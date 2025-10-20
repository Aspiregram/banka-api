package com.banka.api.records.pais;

import java.util.Set;
import java.util.UUID;

public record PaisResponseDto(
        UUID id,
        String nome,
        String isoCode,
        Set<UUID> moedas
) {
}
