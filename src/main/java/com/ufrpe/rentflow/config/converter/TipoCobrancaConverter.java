package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.TipoCobranca;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCobrancaConverter extends EnumConverterBase<TipoCobranca> {
    public TipoCobrancaConverter() { super(TipoCobranca.class); }
}
