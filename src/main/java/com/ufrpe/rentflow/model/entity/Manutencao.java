package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.TipoManutencao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "MANUTENCOES")
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manut")
    private Integer idManut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa_veiculo", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_func", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false)
    private TipoManutencao tipo;

    @Column(nullable = false, length = 200)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada = LocalDate.now();

    @Column(name = "previsao_saida", nullable = false)
    private LocalDate previsaoSaida;

    @Column(name = "data_saida_real")
    private LocalDate dataSaidaReal;

    @Column(precision = 10, scale = 2)
    private BigDecimal custo;

    // Getters e Setters

    public Integer getIdManut() { return idManut; }
    public void setIdManut(Integer idManut) { this.idManut = idManut; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDate getPrevisaoSaida() { return previsaoSaida; }
    public void setPrevisaoSaida(LocalDate previsaoSaida) { this.previsaoSaida = previsaoSaida; }

    public LocalDate getDataSaidaReal() { return dataSaidaReal; }
    public void setDataSaidaReal(LocalDate dataSaidaReal) { this.dataSaidaReal = dataSaidaReal; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manutencao that = (Manutencao) o;
        return Objects.equals(idManut, that.idManut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idManut);
    }

    @Override
    public String toString() {
        return "Manutencao{idManut=" + idManut + ", tipo=" + tipo + ", motivo='" + motivo + "'}";
    }
}
