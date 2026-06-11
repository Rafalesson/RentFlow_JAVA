package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.TipoManutencao;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoManutencaoConverter extends EnumConverterBase<TipoManutencao> {
    public TipoManutencaoConverter() { super(TipoManutencao.class); }
}
