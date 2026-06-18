package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.TipoCobranca;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "COBRANCAS_EXTRAS")
public class CobrancaExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cobranca")
    private Integer idCobranca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loc", nullable = false)
    private Locacao locacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vistoria")
    private Vistoria vistoria;

    @Column(nullable = false)
    private TipoCobranca tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    // Getters e Setters

    public Integer getIdCobranca() { return idCobranca; }
    public void setIdCobranca(Integer idCobranca) { this.idCobranca = idCobranca; }

    public Locacao getLocacao() { return locacao; }
    public void setLocacao(Locacao locacao) { this.locacao = locacao; }

    public Vistoria getVistoria() { return vistoria; }
    public void setVistoria(Vistoria vistoria) { this.vistoria = vistoria; }

    public TipoCobranca getTipo() { return tipo; }
    public void setTipo(TipoCobranca tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CobrancaExtra that = (CobrancaExtra) o;
        return Objects.equals(idCobranca, that.idCobranca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCobranca);
    }

    @Override
    public String toString() {
        return "CobrancaExtra{idCobranca=" + idCobranca + ", tipo=" + tipo + ", valor=" + valor + "}";
    }
}
