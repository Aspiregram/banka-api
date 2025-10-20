package com.banka.api.records.autenticacao;

public record AuthReqDto(
        String username,
        String password
) {
}
