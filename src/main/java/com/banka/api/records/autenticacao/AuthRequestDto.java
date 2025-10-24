package com.banka.api.records.autenticacao;

public record AuthRequestDto(
        String username,
        String password
) {
}
