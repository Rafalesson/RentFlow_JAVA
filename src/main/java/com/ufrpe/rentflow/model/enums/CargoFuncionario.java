package com.ufrpe.rentflow.model.enums;

/**
 * Cargos disponíveis para funcionários do sistema.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum CargoFuncionario implements PersistableEnum {
    ATENDENTE("atendente", "Atendente"),
    GERENTE("gerente", "Gerente");

    private final String valorBanco;
    private final String label;

    CargoFuncionario(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
