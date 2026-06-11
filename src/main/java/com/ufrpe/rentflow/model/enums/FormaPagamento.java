package com.ufrpe.rentflow.model.enums;

/**
 * Formas de pagamento aceitas pelo sistema.
 * Convenção Java: UPPER_SNAKE_CASE. Mapeamento ao PostgreSQL via {@link PersistableEnum}.
 */
public enum FormaPagamento implements PersistableEnum {
    DINHEIRO("dinheiro", "Dinheiro"),
    CREDITO("credito", "Crédito"),
    DEBITO("debito", "Débito"),
    PIX("pix", "Pix");

    private final String valorBanco;
    private final String label;

    FormaPagamento(String valorBanco, String label) {
        this.valorBanco = valorBanco;
        this.label = label;
    }

    @Override
    public String getValorBanco() { return valorBanco; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
