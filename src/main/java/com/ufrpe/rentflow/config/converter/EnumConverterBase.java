package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.PersistableEnum;
import jakarta.persistence.AttributeConverter;

/**
 * Conversor JPA genérico que traduz entre o nome UPPER_SNAKE_CASE do enum Java
 * e o valor lowercase armazenado no PostgreSQL (ENUM nativo).
 *
 * Herança (POO): cada conversor concreto estende esta classe abstrata,
 * reutilizando a lógica de conversão sem duplicação de código.
 *
 * @param <E> Tipo do enum que implementa PersistableEnum
 */
public abstract class EnumConverterBase<E extends Enum<E> & PersistableEnum>
        implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected EnumConverterBase(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getValorBanco();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValorBanco().equals(dbData)) return e;
        }
        throw new IllegalArgumentException(
                "Valor desconhecido para " + enumClass.getSimpleName() + ": " + dbData);
    }
}
