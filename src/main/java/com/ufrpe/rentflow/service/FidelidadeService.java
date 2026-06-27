package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.entity.MovimentacaoPontos;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import com.ufrpe.rentflow.model.enums.TipoMovimentacaoPontos;
import com.ufrpe.rentflow.repository.LocacaoRepository;
import com.ufrpe.rentflow.repository.MovimentacaoPontosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class FidelidadeService {

    // A cada R$ 10,00 pagos, o cliente recebe 1 ponto.
    private static final BigDecimal VALOR_POR_PONTO =
            new BigDecimal("10.00");

    // Cada 100 pontos podem ser trocados por R$ 20,00.
    private static final int PONTOS_POR_RESGATE = 100;

    private static final BigDecimal DESCONTO_POR_RESGATE =
            new BigDecimal("20.00");

    private final MovimentacaoPontosRepository movimentacaoRepository;
    private final LocacaoRepository locacaoRepository;

    public FidelidadeService(
            MovimentacaoPontosRepository movimentacaoRepository,
            LocacaoRepository locacaoRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.locacaoRepository = locacaoRepository;
    }

    /**
     * Registra os pontos gerados por uma locação encerrada.
     */
    @Transactional
    public void creditarPontosPorLocacao(Integer idLocacao) {

        Locacao locacao = locacaoRepository.findById(idLocacao)
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "Locação não encontrada."
                        )
                );

        if (locacao.getStatus() != StatusLocacao.ENCERRADA) {
            throw new RegraNegocioException(
                    "Os pontos só podem ser gerados após o encerramento da locação."
            );
        }

        if (locacao.getValorTotal() == null
                || locacao.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RegraNegocioException(
                    "A locação não possui um valor válido para gerar pontos."
            );
        }

        boolean jaCreditado =
                movimentacaoRepository.existsByLocacao_IdLocAndTipo(
                        idLocacao,
                        TipoMovimentacaoPontos.CREDITO
                );

        // Evita crédito duplicado.
        if (jaCreditado) {
            return;
        }

        int pontosGerados = calcularPontos(locacao.getValorTotal());

        // Uma locação abaixo de R$ 10,00 não gera pontos.
        if (pontosGerados <= 0) {
            return;
        }

        MovimentacaoPontos movimentacao = new MovimentacaoPontos();

        movimentacao.setCliente(locacao.getCliente());
        movimentacao.setLocacao(locacao);
        movimentacao.setTipo(TipoMovimentacaoPontos.CREDITO);
        movimentacao.setQuantidade(pontosGerados);
        movimentacao.setDataHora(OffsetDateTime.now());
        movimentacao.setDescricao(
                "Crédito referente à locação #" + idLocacao
        );

        movimentacaoRepository.save(movimentacao);
    }

    /**
     * Calcula o saldo atual do cliente.
     */
    @Transactional(readOnly = true)
    public int calcularSaldo(String cpf) {

        List<MovimentacaoPontos> movimentacoes =
                movimentacaoRepository
                        .findByCliente_CpfOrderByDataHoraDesc(cpf);

        int saldo = 0;

        for (MovimentacaoPontos movimentacao : movimentacoes) {

            if (movimentacao.getTipo()
                    == TipoMovimentacaoPontos.CREDITO) {

                saldo += movimentacao.getQuantidade();

            } else if (movimentacao.getTipo()
                    == TipoMovimentacaoPontos.RESGATE) {

                saldo -= movimentacao.getQuantidade();
            }
        }

        return saldo;
    }

    /**
     * Registra o resgate dos pontos e retorna o desconto obtido.
     */
    @Transactional
    public BigDecimal resgatarPontosParaLocacao(
            Locacao locacao,
            int quantidade
    ) {
        if (locacao == null || locacao.getCliente() == null) {
            throw new RegraNegocioException(
                    "A locação e o cliente devem ser informados."
            );
        }

        if (locacao.getIdLoc() == null) {
            throw new RegraNegocioException(
                    "A locação precisa estar registrada antes do resgate."
            );
        }

        if (quantidade <= 0) {
            throw new RegraNegocioException(
                    "A quantidade de pontos deve ser maior que zero."
            );
        }

        if (quantidade % PONTOS_POR_RESGATE != 0) {
            throw new RegraNegocioException(
                    "Os pontos devem ser resgatados em grupos de 100."
            );
        }

        boolean jaResgatado =
                movimentacaoRepository.existsByLocacao_IdLocAndTipo(
                        locacao.getIdLoc(),
                        TipoMovimentacaoPontos.RESGATE
                );

        if (jaResgatado) {
            throw new RegraNegocioException(
                    "Esta locação já possui um resgate de pontos."
            );
        }

        String cpf = locacao.getCliente().getCpf();
        int saldoAtual = calcularSaldo(cpf);

        if (quantidade > saldoAtual) {
            throw new RegraNegocioException(
                    "O cliente não possui pontos suficientes."
            );
        }

        int quantidadeResgates =
                quantidade / PONTOS_POR_RESGATE;

        BigDecimal desconto =
                DESCONTO_POR_RESGATE.multiply(
                        BigDecimal.valueOf(quantidadeResgates)
                );

        MovimentacaoPontos movimentacao =
                new MovimentacaoPontos();

        movimentacao.setCliente(locacao.getCliente());
        movimentacao.setLocacao(locacao);
        movimentacao.setTipo(TipoMovimentacaoPontos.RESGATE);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDataHora(OffsetDateTime.now());
        movimentacao.setDescricao(
                "Resgate aplicado à locação #"
                        + locacao.getIdLoc()
                        + " — desconto de R$ "
                        + desconto
        );

        movimentacaoRepository.save(movimentacao);

        return desconto;
    }

    /**
     * Retorna o histórico completo de pontos do cliente.
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoPontos> listarHistorico(String cpf) {
        return movimentacaoRepository
                .findByCliente_CpfOrderByDataHoraDesc(cpf);
    }

    private int calcularPontos(BigDecimal valorTotal) {
        return valorTotal
                .divide(
                        VALOR_POR_PONTO,
                        0,
                        RoundingMode.DOWN
                )
                .intValue();
    }
}