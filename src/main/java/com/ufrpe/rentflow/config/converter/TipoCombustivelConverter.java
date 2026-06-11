package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.TipoCombustivel;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCombustivelConverter extends EnumConverterBase<TipoCombustivel> {
    public TipoCombustivelConverter() { super(TipoCombustivel.class); }
}
