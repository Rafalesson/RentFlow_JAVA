package com.ufrpe.rentflow.model.enums;

/**
 * Status possíveis de uma locação.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum StatusLocacao implements PersistableEnum {
    RESERVADA("reservada", "Reservada"),
    ATIVA("ativa", "Ativa"),
    ENCERRADA("encerrada", "Encerrada"),
    CANCELADA("cancelada", "Cancelada");

    private final String valorBanco;
    private final String label;

    StatusLocacao(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
