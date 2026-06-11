package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.StatusLocacao;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusLocacaoConverter extends EnumConverterBase<StatusLocacao> {
    public StatusLocacaoConverter() { super(StatusLocacao.class); }
}
