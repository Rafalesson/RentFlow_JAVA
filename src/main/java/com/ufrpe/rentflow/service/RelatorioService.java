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
import com.ufrpe.rentflow.model.dto.HistoricoVeiculoDTO;
import com.ufrpe.rentflow.model.dto.RelatorioVeiculoDTO;
import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.entity.Vistoria;
import com.ufrpe.rentflow.model.enums.TipoVistoria;
import com.ufrpe.rentflow.repository.VeiculoRepository;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class RelatorioService {

    private final ClienteRepository clienteRepository;
    private final LocacaoRepository locacaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final VistoriaService vistoriaService;

    public RelatorioService(
            ClienteRepository clienteRepository,
            LocacaoRepository locacaoRepository, VeiculoRepository veiculoRepository, VistoriaService vistoriaService
    ) {
        this.clienteRepository = clienteRepository;
        this.locacaoRepository = locacaoRepository;
        this.veiculoRepository = veiculoRepository;
        this.vistoriaService = vistoriaService;
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

    @Transactional(readOnly = true)
    public RelatorioVeiculoDTO gerarRelatorioVeiculo(String placa) {

        if (placa == null || placa.isBlank()) {
            throw new RegraNegocioException(
                    "Informe a placa do veículo para gerar o relatório."
            );
        }

        Veiculo veiculo = veiculoRepository.findById(placa)
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "Veículo não encontrado."
                        )
                );

        List<Locacao> locacoes = locacaoRepository
                .findByVeiculo_PlacaOrderByDataReservaDesc(placa);

        List<HistoricoVeiculoDTO> historico = new ArrayList<>();

        int quilometrosPercorridosTotal = 0;
        int combustivelConsumidoTotal = 0;

        for (Locacao locacao : locacoes) {

            Vistoria retirada = vistoriaService
                    .buscarPorLocacaoETipo(
                            locacao.getIdLoc(),
                            TipoVistoria.RETIRADA
                    )
                    .orElse(null);

            Vistoria devolucao = vistoriaService
                    .buscarPorLocacaoETipo(
                            locacao.getIdLoc(),
                            TipoVistoria.DEVOLUCAO
                    )
                    .orElse(null);

            int quilometrosPercorridos = 0;
            int combustivelConsumido = 0;

            if (retirada != null && devolucao != null) {

                quilometrosPercorridos = Math.max(
                        0,
                        devolucao.getKm() - retirada.getKm()
                );

                combustivelConsumido = Math.max(
                        0,
                        retirada.getNivelCombustivel()
                                - devolucao.getNivelCombustivel()
                );
            }

            quilometrosPercorridosTotal += quilometrosPercorridos;
            combustivelConsumidoTotal += combustivelConsumido;

            historico.add(
                    new HistoricoVeiculoDTO(
                            locacao,
                            retirada,
                            devolucao,
                            quilometrosPercorridos,
                            combustivelConsumido
                    )
            );
        }

        long locacoesEncerradas = locacoes.stream()
                .filter(locacao ->
                        locacao.getStatus() == StatusLocacao.ENCERRADA
                )
                .count();

        BigDecimal receitaTotal = locacoes.stream()
                .filter(locacao ->
                        locacao.getStatus() == StatusLocacao.ENCERRADA
                )
                .map(Locacao::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioVeiculoDTO(
                veiculo,
                locacoes.size(),
                locacoesEncerradas,
                quilometrosPercorridosTotal,
                combustivelConsumidoTotal,
                receitaTotal,
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