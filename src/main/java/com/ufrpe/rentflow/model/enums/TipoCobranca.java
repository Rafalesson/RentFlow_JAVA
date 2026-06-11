package com.ufrpe.rentflow.model.enums;

/**
 * Tipos de cobrança extra em uma locação.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum TipoCobranca implements PersistableEnum {
    AVARIA("avaria", "Avaria"),
    COMBUSTIVEL("combustivel", "Combustível"),
    MULTA("multa", "Multa"),
    OUTROS("outros", "Outros");

    private final String valorBanco;
    private final String label;

    TipoCobranca(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
