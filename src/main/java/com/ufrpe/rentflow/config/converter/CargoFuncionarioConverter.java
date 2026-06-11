package com.ufrpe.rentflow.config.converter;

import com.ufrpe.rentflow.model.enums.CargoFuncionario;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CargoFuncionarioConverter extends EnumConverterBase<CargoFuncionario> {
    public CargoFuncionarioConverter() { super(CargoFuncionario.class); }
}
