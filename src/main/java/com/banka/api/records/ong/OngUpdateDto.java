package com.banka.api.records.ong;

import com.banka.api.records.usuario.UsuarioUpdateDto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record OngUpdateDto(
        UsuarioUpdateDto usuUptDto,

        @Size(max = 30, message = "Telefone não deve ultrapassar 30 caracteres em tamanho")
        String telefone,
        UUID pais,

        @Digits(integer = 15, fraction = 2,
                message = "Saldo global não deve ultrapassar" +
                        "de 15 unidades e 2 casas após a vírgula")
        BigDecimal saldoGlobal
) {
}
