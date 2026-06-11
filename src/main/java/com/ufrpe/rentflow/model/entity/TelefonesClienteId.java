package com.ufrpe.rentflow.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class TelefonesClienteId implements Serializable {

    private String cliente; // Matches the property name in TelefonesCliente
    private String numero;

    public TelefonesClienteId() {}

    public TelefonesClienteId(String cliente, String numero) {
        this.cliente = cliente;
        this.numero = numero;
    }

    // Getters, Setters, hashCode, equals

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelefonesClienteId that = (TelefonesClienteId) o;
        return Objects.equals(cliente, that.cliente) &&
               Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, numero);
    }
}
