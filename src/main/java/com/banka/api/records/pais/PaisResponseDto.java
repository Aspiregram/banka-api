package com.banka.api.records.pais;

public record PaisResponseDto(
        Long id,
        String nome,
        String isoCode
) {
}
