package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.TipoVistoria;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoVistoriaConverter extends EnumConverterBase<TipoVistoria> {
    public TipoVistoriaConverter() { super(TipoVistoria.class); }
}
