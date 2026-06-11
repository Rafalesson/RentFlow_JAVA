package com.ufrpe.rentflow.model.enums;

/**
 * Status possíveis de um veículo na frota.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum StatusVeiculo implements PersistableEnum {
    DISPONIVEL("disponivel", "Disponível"),
    LOCADO("locado", "Locado"),
    EM_MANUTENCAO("em_manutencao", "Em Manutenção"),
    INATIVO("inativo", "Inativo");

    private final String valorBanco;
    private final String label;

    StatusVeiculo(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
