package com.ufrpe.rentflow.model.enums;

/**
 * Tipos de vistoria realizados em uma locação.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum TipoVistoria implements PersistableEnum {
    RETIRADA("retirada", "Retirada"),
    DEVOLUCAO("devolucao", "Devolução");

    private final String valorBanco;
    private final String label;

    TipoVistoria(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
