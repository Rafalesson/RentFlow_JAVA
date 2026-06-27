package com.ufrpe.rentflow.model.dto;

import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.model.entity.Locacao;

import java.math.BigDecimal;
import java.util.List;

/**
 * Transporta os dados calculados do relatório de um cliente.
 * Esta classe não representa uma tabela do banco de dados.
 */
public class RelatorioClienteDTO {

    private final Cliente cliente;

    private final long totalLocacoes;
    private final long locacoesReservadas;
    private final long locacoesAtivas;
    private final long locacoesEncerradas;
    private final long locacoesCanceladas;

    private final BigDecimal valorTotalGasto;
    private final BigDecimal ticketMedio;

    private final Locacao ultimaLocacao;
    private final List<Locacao> historico;

    public RelatorioClienteDTO(
            Cliente cliente,
            long totalLocacoes,
            long locacoesReservadas,
            long locacoesAtivas,
            long locacoesEncerradas,
            long locacoesCanceladas,
            BigDecimal valorTotalGasto,
            BigDecimal ticketMedio,
            Locacao ultimaLocacao,
            List<Locacao> historico
    ) {
        this.cliente = cliente;
        this.totalLocacoes = totalLocacoes;
        this.locacoesReservadas = locacoesReservadas;
        this.locacoesAtivas = locacoesAtivas;
        this.locacoesEncerradas = locacoesEncerradas;
        this.locacoesCanceladas = locacoesCanceladas;
        this.valorTotalGasto = valorTotalGasto;
        this.ticketMedio = ticketMedio;
        this.ultimaLocacao = ultimaLocacao;
        this.historico = historico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public long getTotalLocacoes() {
        return totalLocacoes;
    }

    public long getLocacoesReservadas() {
        return locacoesReservadas;
    }

    public long getLocacoesAtivas() {
        return locacoesAtivas;
    }

    public long getLocacoesEncerradas() {
        return locacoesEncerradas;
    }

    public long getLocacoesCanceladas() {
        return locacoesCanceladas;
    }

    public BigDecimal getValorTotalGasto() {
        return valorTotalGasto;
    }

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }

    public Locacao getUltimaLocacao() {
        return ultimaLocacao;
    }

    public List<Locacao> getHistorico() {
        return historico;
    }
}