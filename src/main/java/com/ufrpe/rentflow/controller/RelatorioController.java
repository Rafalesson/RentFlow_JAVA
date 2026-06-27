package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.dto.RelatorioClienteDTO;
import com.ufrpe.rentflow.service.ClienteService;
import com.ufrpe.rentflow.service.RelatorioService;
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

    public RelatorioController(
            RelatorioService relatorioService,
            ClienteService clienteService
    ) {
        this.relatorioService = relatorioService;
        this.clienteService = clienteService;
    }

    /**
     * Abre a página principal de relatórios.
     *
     * Quando nenhum CPF é informado, mostra apenas
     * o seletor de clientes.
     *
     * Quando um CPF é informado, também gera
     * o relatório daquele cliente.
     */
    @GetMapping
    public String exibirRelatorios(
            @RequestParam(
                    value = "cpf",
                    required = false
            ) String cpf,
            Model model
    ) {
        model.addAttribute("activePage", "relatorios");

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        model.addAttribute(
                "cpfSelecionado",
                cpf
        );

        // restante do método...

        if (cpf != null && !cpf.isBlank()) {
            try {
                RelatorioClienteDTO relatorioCliente =
                        relatorioService.gerarRelatorioCliente(cpf);

                model.addAttribute(
                        "relatorioCliente",
                        relatorioCliente
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