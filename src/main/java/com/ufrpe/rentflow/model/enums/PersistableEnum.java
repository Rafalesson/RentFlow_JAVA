package com.ufrpe.rentflow.model.enums;

/**
 * Interface para enums que possuem representação diferente no banco de dados.
 * Permite mapear UPPER_SNAKE_CASE (Java) para lowercase (PostgreSQL ENUM nativo).
 *
 * Exemplo de uso: StatusVeiculo.DISPONIVEL → "disponivel" no banco.
 */
public interface PersistableEnum {
    String getValorBanco();
}
