package com.ufrpe.rentflow.model.enums;

/**
 * Tipos de combustível aceitos pela frota.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum TipoCombustivel implements PersistableEnum {
    GASOLINA("gasolina", "Gasolina"),
    ETANOL("etanol", "Etanol"),
    FLEX("flex", "Flex"),
    DIESEL("diesel", "Diesel"),
    ELETRICO("eletrico", "Elétrico");

    private final String valorBanco;
    private final String label;

    TipoCombustivel(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
