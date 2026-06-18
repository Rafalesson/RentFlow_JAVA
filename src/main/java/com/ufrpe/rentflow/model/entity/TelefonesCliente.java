package com.ufrpe.rentflow.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TELEFONES_CLIENTE")
@IdClass(TelefonesClienteId.class)
public class TelefonesCliente {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_cliente", nullable = false)
    private Cliente cliente;

    @Id
    @Column(length = 20)
    private String numero;

    @Column(nullable = false, length = 20)
    private String tipo;

    // Getters e Setters

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "TelefonesCliente{numero='" + numero + "', tipo='" + tipo + "'}";
    }
}
