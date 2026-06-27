package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.TipoMovimentacaoPontos;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "MOVIMENTACOES_PONTOS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_movimentacao_locacao_tipo",
                        columnNames = {"id_loc", "tipo"}
                )
        }
)
public class MovimentacaoPontos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Integer idMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_cliente", nullable = false)
    private Cliente cliente;

    /*
     * Pode ser nula porque alguns ajustes manuais ou resgates
     * podem não estar vinculados diretamente a uma locação.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loc")
    private Locacao locacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacaoPontos tipo;

    /*
     * A quantidade será sempre positiva.
     * O tipo define se os pontos entram ou saem do saldo.
     */
    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private OffsetDateTime dataHora = OffsetDateTime.now();

    @Column(nullable = false, length = 255)
    private String descricao;

    public Integer getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Integer idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Locacao getLocacao() {
        return locacao;
    }

    public void setLocacao(Locacao locacao) {
        this.locacao = locacao;
    }

    public TipoMovimentacaoPontos getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacaoPontos tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovimentacaoPontos that = (MovimentacaoPontos) o;

        return Objects.equals(
                idMovimentacao,
                that.idMovimentacao
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimentacao);
    }

    @Override
    public String toString() {
        return "MovimentacaoPontos{" +
                "idMovimentacao=" + idMovimentacao +
                ", tipo=" + tipo +
                ", quantidade=" + quantidade +
                ", dataHora=" + dataHora +
                '}';
    }
}