package com.ufrpe.rentflow.exception;

// Uma exceção customizada para regras de negócio - Exemplo clássico de Herança em POO
public class RegraNegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
