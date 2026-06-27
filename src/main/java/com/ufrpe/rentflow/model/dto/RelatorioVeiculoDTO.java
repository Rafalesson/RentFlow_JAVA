package com.ufrpe.rentflow.model.dto;

import com.ufrpe.rentflow.model.entity.Veiculo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Transporta os indicadores e o histórico de utilização de um veículo.
 * Não representa uma tabela do banco de dados.
 */
public class RelatorioVeiculoDTO {

    private final Veiculo veiculo;

    private final long totalLocacoes;
    private final long locacoesEncerradas;

    private final int quilometrosPercorridosTotal;
    private final int combustivelConsumidoTotal;

    private final BigDecimal receitaTotal;

    private final List<HistoricoVeiculoDTO> historico;

    public RelatorioVeiculoDTO(
            Veiculo veiculo,
            long totalLocacoes,
            long locacoesEncerradas,
            int quilometrosPercorridosTotal,
            int combustivelConsumidoTotal,
            BigDecimal receitaTotal,
            List<HistoricoVeiculoDTO> historico
    ) {
        this.veiculo = veiculo;
        this.totalLocacoes = totalLocacoes;
        this.locacoesEncerradas = locacoesEncerradas;
        this.quilometrosPercorridosTotal = quilometrosPercorridosTotal;
        this.combustivelConsumidoTotal = combustivelConsumidoTotal;
        this.receitaTotal = receitaTotal;
        this.historico = historico;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public long getTotalLocacoes() {
        return totalLocacoes;
    }

    public long getLocacoesEncerradas() {
        return locacoesEncerradas;
    }

    public int getQuilometrosPercorridosTotal() {
        return quilometrosPercorridosTotal;
    }

    public int getCombustivelConsumidoTotal() {
        return combustivelConsumidoTotal;
    }

    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }

    public List<HistoricoVeiculoDTO> getHistorico() {
        return historico;
    }
}