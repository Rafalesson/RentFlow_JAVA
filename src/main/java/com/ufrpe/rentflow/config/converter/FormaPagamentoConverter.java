package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.FormaPagamento;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FormaPagamentoConverter extends EnumConverterBase<FormaPagamento> {
    public FormaPagamentoConverter() { super(FormaPagamento.class); }
}
