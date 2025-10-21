package com.banka.api.records.ong;

import com.banka.api.records.usuario.UsuarioCreateDto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OngCreateDto(
        UsuarioCreateDto usuCreateDto,

        @NotBlank(message = "Telefone não deve ser nulo, vazio ou em branco")
        @Size(max = 30, message = "Telefone não deve ultrapassar 30 caracteres em tamanho")
        String telefone,

        @NotBlank(message = "País não deve ser nulo, vazio ou em branco")
        Long pais,

        @NotBlank(message = "Saldo global não deve ser nulo, vazio ou em branco")
        @Digits(integer = 15, fraction = 2,
                message = "Saldo global não deve ultrapassar" +
                        "de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldoGlobal
) {
}
