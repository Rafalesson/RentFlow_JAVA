package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.dto.RelatorioClienteDTO;
import com.ufrpe.rentflow.model.dto.RelatorioVeiculoDTO;
import com.ufrpe.rentflow.service.ClienteService;
import com.ufrpe.rentflow.service.RelatorioService;
import com.ufrpe.rentflow.service.VeiculoService;
import com.ufrpe.rentflow.service.FidelidadeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final FidelidadeService fidelidadeService;

    public RelatorioController(
            RelatorioService relatorioService,
            ClienteService clienteService,
            VeiculoService veiculoService, FidelidadeService fidelidadeService
    ) {
        this.relatorioService = relatorioService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.fidelidadeService = fidelidadeService;
    }

    @GetMapping
    public String exibirRelatorios(
            @RequestParam(
                    value = "cpf",
                    required = false
            ) String cpf,

            @RequestParam(
                    value = "placa",
                    required = false
            ) String placa,

            Model model
    ) {
        model.addAttribute(
                "activePage",
                "relatorios"
        );

        // Listas utilizadas nos dois seletores
        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        model.addAttribute(
                "veiculos",
                veiculoService.listarTodos()
        );

        // Mantém selecionadas as opções após gerar o relatório
        model.addAttribute(
                "cpfSelecionado",
                cpf
        );

        model.addAttribute(
                "placaSelecionada",
                placa
        );

        // Relatório do cliente
        if (cpf != null && !cpf.isBlank()) {
            try {
                RelatorioClienteDTO relatorioCliente =
                        relatorioService.gerarRelatorioCliente(cpf);

                model.addAttribute(
                        "relatorioCliente",
                        relatorioCliente
                );
                model.addAttribute(
                        "saldoPontos",
                        fidelidadeService.calcularSaldo(cpf)
                );

                model.addAttribute(
                        "historicoPontos",
                        fidelidadeService.listarHistorico(cpf)
                );

            } catch (RegraNegocioException e) {
                model.addAttribute(
                        "errorMessage",
                        e.getMessage()
                );
            }
        }

        // Relatório do veículo
        if (placa != null && !placa.isBlank()) {
            try {
                RelatorioVeiculoDTO relatorioVeiculo =
                        relatorioService.gerarRelatorioVeiculo(placa);

                model.addAttribute(
                        "relatorioVeiculo",
                        relatorioVeiculo
                );

            } catch (RegraNegocioException e) {
                model.addAttribute(
                        "errorMessage",
                        e.getMessage()
                );
            }
        }

        return "relatorios/index";
    }
}