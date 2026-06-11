package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.StatusLocacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "LOCACOES")
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loc")
    private Integer idLoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa_veiculo", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_func_registro", nullable = false)
    private Funcionario funcionarioRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_func_autoriza")
    private Funcionario funcionarioAutoriza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_func_devolucao")
    private Funcionario funcionarioDevolucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro", nullable = false)
    private Seguro seguro;

    @Column(nullable = false)
    private StatusLocacao status = StatusLocacao.RESERVADA;

    @Column(name = "data_reserva", nullable = false, updatable = false)
    private OffsetDateTime dataReserva = OffsetDateTime.now();

    @Column(name = "data_retirada")
    private OffsetDateTime dataRetirada;

    @Column(name = "data_devol_prevista", nullable = false)
    private OffsetDateTime dataDevolPrevista;

    @Column(name = "data_devol_real")
    private OffsetDateTime dataDevolReal;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    // Getters and Setters

    public Integer getIdLoc() { return idLoc; }
    public void setIdLoc(Integer idLoc) { this.idLoc = idLoc; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Funcionario getFuncionarioRegistro() { return funcionarioRegistro; }
    public void setFuncionarioRegistro(Funcionario funcionarioRegistro) { this.funcionarioRegistro = funcionarioRegistro; }

    public Funcionario getFuncionarioAutoriza() { return funcionarioAutoriza; }
    public void setFuncionarioAutoriza(Funcionario funcionarioAutoriza) { this.funcionarioAutoriza = funcionarioAutoriza; }

    public Funcionario getFuncionarioDevolucao() { return funcionarioDevolucao; }
    public void setFuncionarioDevolucao(Funcionario funcionarioDevolucao) { this.funcionarioDevolucao = funcionarioDevolucao; }

    public Seguro getSeguro() { return seguro; }
    public void setSeguro(Seguro seguro) { this.seguro = seguro; }

    public StatusLocacao getStatus() { return status; }
    public void setStatus(StatusLocacao status) { this.status = status; }

    public OffsetDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(OffsetDateTime dataReserva) { this.dataReserva = dataReserva; }

    public OffsetDateTime getDataRetirada() { return dataRetirada; }
    public void setDataRetirada(OffsetDateTime dataRetirada) { this.dataRetirada = dataRetirada; }

    public OffsetDateTime getDataDevolPrevista() { return dataDevolPrevista; }
    public void setDataDevolPrevista(OffsetDateTime dataDevolPrevista) { this.dataDevolPrevista = dataDevolPrevista; }

    public OffsetDateTime getDataDevolReal() { return dataDevolReal; }
    public void setDataDevolReal(OffsetDateTime dataDevolReal) { this.dataDevolReal = dataDevolReal; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locacao that = (Locacao) o;
        return Objects.equals(idLoc, that.idLoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLoc);
    }

    @Override
    public String toString() {
        return "Locacao{idLoc=" + idLoc + ", status=" + status + ", dataReserva=" + dataReserva + "}";
    }
}
