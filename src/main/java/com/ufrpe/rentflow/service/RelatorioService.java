package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.dto.RelatorioClienteDTO;
import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import com.ufrpe.rentflow.repository.ClienteRepository;
import com.ufrpe.rentflow.repository.LocacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class RelatorioService {

    private final ClienteRepository clienteRepository;
    private final LocacaoRepository locacaoRepository;

    public RelatorioService(
            ClienteRepository clienteRepository,
            LocacaoRepository locacaoRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.locacaoRepository = locacaoRepository;
    }

    /**
     * Gera os indicadores e o histórico completo de um cliente.
     */
    @Transactional(readOnly = true)
    public RelatorioClienteDTO gerarRelatorioCliente(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new RegraNegocioException(
                    "Informe o CPF do cliente para gerar o relatório."
            );
        }

        Cliente cliente = clienteRepository.findById(cpf)
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "Cliente não encontrado."
                        )
                );

        List<Locacao> historico = locacaoRepository
                .findByCliente_CpfOrderByDataReservaDesc(cpf);

        long totalLocacoes = historico.size();

        long locacoesReservadas =
                contarPorStatus(historico, StatusLocacao.RESERVADA);

        long locacoesAtivas =
                contarPorStatus(historico, StatusLocacao.ATIVA);

        long locacoesEncerradas =
                contarPorStatus(historico, StatusLocacao.ENCERRADA);

        long locacoesCanceladas =
                contarPorStatus(historico, StatusLocacao.CANCELADA);

        BigDecimal valorTotalGasto = historico.stream()
                .filter(locacao ->
                        locacao.getStatus() == StatusLocacao.ENCERRADA
                )
                .map(Locacao::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long locacoesEncerradasComValor = historico.stream()
                .filter(locacao ->
                        locacao.getStatus() == StatusLocacao.ENCERRADA
                                && locacao.getValorTotal() != null
                )
                .count();

        BigDecimal ticketMedio = calcularTicketMedio(
                valorTotalGasto,
                locacoesEncerradasComValor
        );

        Locacao ultimaLocacao = historico.isEmpty()
                ? null
                : historico.get(0);

        return new RelatorioClienteDTO(
                cliente,
                totalLocacoes,
                locacoesReservadas,
                locacoesAtivas,
                locacoesEncerradas,
                locacoesCanceladas,
                valorTotalGasto,
                ticketMedio,
                ultimaLocacao,
                historico
        );
    }

    private long contarPorStatus(
            List<Locacao> locacoes,
            StatusLocacao status
    ) {
        return locacoes.stream()
                .filter(locacao -> locacao.getStatus() == status)
                .count();
    }

    private BigDecimal calcularTicketMedio(
            BigDecimal valorTotal,
            long quantidadeLocacoes
    ) {
        if (quantidadeLocacoes == 0) {
            return BigDecimal.ZERO;
        }

        return valorTotal.divide(
                BigDecimal.valueOf(quantidadeLocacoes),
                2,
                RoundingMode.HALF_UP
        );
    }
}