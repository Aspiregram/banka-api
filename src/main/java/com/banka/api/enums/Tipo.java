package com.banka.api.enums;

public enum Tipo {
    TIPO_DOACAO,
    TIPO_RECEBIMENTO,
    TIPO_TRANSFERENCIA;

    public static Tipo fromString(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "DOACAO" -> TIPO_DOACAO;
            case "REBECIMENTO" -> TIPO_RECEBIMENTO;
            case "TRANSFERENCIA" -> TIPO_TRANSFERENCIA;
            default -> throw new IllegalArgumentException("O tipo \"" + tipo
                    + "\" não é um enumerador válido");
        };
    }
}
