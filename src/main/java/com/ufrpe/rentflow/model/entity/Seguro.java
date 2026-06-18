package com.ufrpe.rentflow.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "SEGUROS")
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguro")
    private Integer idSeguro;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao_cobertura", nullable = false, columnDefinition = "TEXT")
    private String descricaoCobertura;

    @Column(name = "valor_diario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiario;

    // Getters e Setters

    public Integer getIdSeguro() { return idSeguro; }
    public void setIdSeguro(Integer idSeguro) { this.idSeguro = idSeguro; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricaoCobertura() { return descricaoCobertura; }
    public void setDescricaoCobertura(String descricaoCobertura) { this.descricaoCobertura = descricaoCobertura; }

    public BigDecimal getValorDiario() { return valorDiario; }
    public void setValorDiario(BigDecimal valorDiario) { this.valorDiario = valorDiario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seguro that = (Seguro) o;
        return Objects.equals(idSeguro, that.idSeguro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSeguro);
    }

    @Override
    public String toString() {
        return "Seguro{idSeguro=" + idSeguro + ", nome='" + nome + "'}";
    }
}
