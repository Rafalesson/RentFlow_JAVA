package com.ufrpe.rentflow.model.enums;

/**
 * Categorias de CNH conforme padrão DETRAN. Já utiliza UPPER_CASE natural.
 * Compatível com ENUM nativo do PostgreSQL via @JdbcTypeCode(NAMED_ENUM).
 */
public enum CategoriaCnh {
    A, B, C, D, E, AB, AC, AD, AE
}
