package com.ufrpe.rentflow.model.enums;

/**
 * Tipos de manutenção realizados nos veículos da frota.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum TipoManutencao implements PersistableEnum {
    PREVENTIVA("preventiva", "Preventiva"),
    CORRETIVA("corretiva", "Corretiva");

    private final String valorBanco;
    private final String label;

    TipoManutencao(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
