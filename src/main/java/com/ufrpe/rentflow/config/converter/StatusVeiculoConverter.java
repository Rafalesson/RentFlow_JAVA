package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusVeiculoConverter extends EnumConverterBase<StatusVeiculo> {
    public StatusVeiculoConverter() { super(StatusVeiculo.class); }
}
