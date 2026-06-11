package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.model.enums.TipoCombustivel;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "VEICULOS")
public class Veiculo {

    @Id
    @Column(length = 8)
    private String placa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cat", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 11, unique = true)
    private String renavam;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String cor;

    @Column(name = "ano_fabricacao", nullable = false)
    private Short anoFabricacao;

    @Column(name = "tipo_combustivel", nullable = false)
    private TipoCombustivel tipoCombustivel;

    @Column(name = "km_atual", nullable = false)
    private Integer kmAtual = 0;

    @Column(name = "nivel_combustivel", nullable = false)
    private Short nivelCombustivel = 100;

    @Column(nullable = false)
    private StatusVeiculo status = StatusVeiculo.DISPONIVEL;

    // Getters and Setters

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getRenavam() { return renavam; }
    public void setRenavam(String renavam) { this.renavam = renavam; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Short getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Short anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public TipoCombustivel getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }

    public Integer getKmAtual() { return kmAtual; }
    public void setKmAtual(Integer kmAtual) { this.kmAtual = kmAtual; }

    public Short getNivelCombustivel() { return nivelCombustivel; }
    public void setNivelCombustivel(Short nivelCombustivel) { this.nivelCombustivel = nivelCombustivel; }

    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }

    // POO: equals e hashCode baseados na chave natural (placa)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo that = (Veiculo) o;
        return Objects.equals(placa, that.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }

    @Override
    public String toString() {
        return "Veiculo{placa='" + placa + "', marca='" + marca + "', modelo='" + modelo + "', status=" + status + "}";
    }
}
