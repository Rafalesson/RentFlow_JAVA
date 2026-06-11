package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.TipoVistoria;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "VISTORIAS")
public class Vistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vistoria")
    private Integer idVistoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loc", nullable = false)
    private Locacao locacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_func", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false)
    private TipoVistoria tipo;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private OffsetDateTime dataHora = OffsetDateTime.now();

    @Column(nullable = false)
    private Integer km;

    @Column(name = "nivel_combustivel", nullable = false)
    private Short nivelCombustivel;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Getters and Setters

    public Integer getIdVistoria() { return idVistoria; }
    public void setIdVistoria(Integer idVistoria) { this.idVistoria = idVistoria; }

    public Locacao getLocacao() { return locacao; }
    public void setLocacao(Locacao locacao) { this.locacao = locacao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public TipoVistoria getTipo() { return tipo; }
    public void setTipo(TipoVistoria tipo) { this.tipo = tipo; }

    public OffsetDateTime getDataHora() { return dataHora; }
    public void setDataHora(OffsetDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getKm() { return km; }
    public void setKm(Integer km) { this.km = km; }

    public Short getNivelCombustivel() { return nivelCombustivel; }
    public void setNivelCombustivel(Short nivelCombustivel) { this.nivelCombustivel = nivelCombustivel; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vistoria that = (Vistoria) o;
        return Objects.equals(idVistoria, that.idVistoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVistoria);
    }

    @Override
    public String toString() {
        return "Vistoria{idVistoria=" + idVistoria + ", tipo=" + tipo + ", dataHora=" + dataHora + "}";
    }
}
