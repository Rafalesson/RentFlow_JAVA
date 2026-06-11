package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.FormaPagamento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "PAGAMENTOS")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer idPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loc", nullable = false)
    private Locacao locacao;

    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime data = OffsetDateTime.now();

    // Getters and Setters

    public Integer getIdPagamento() { return idPagamento; }
    public void setIdPagamento(Integer idPagamento) { this.idPagamento = idPagamento; }

    public Locacao getLocacao() { return locacao; }
    public void setLocacao(Locacao locacao) { this.locacao = locacao; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public OffsetDateTime getData() { return data; }
    public void setData(OffsetDateTime data) { this.data = data; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento that = (Pagamento) o;
        return Objects.equals(idPagamento, that.idPagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPagamento);
    }

    @Override
    public String toString() {
        return "Pagamento{idPagamento=" + idPagamento + ", formaPagamento=" + formaPagamento + ", valor=" + valor + "}";
    }
}
