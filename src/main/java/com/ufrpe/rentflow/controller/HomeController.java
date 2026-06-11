package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.service.ClienteService;
import com.ufrpe.rentflow.service.LocacaoService;
import com.ufrpe.rentflow.service.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final VeiculoService veiculoService;
    private final ClienteService clienteService;
    private final LocacaoService locacaoService;

    public HomeController(VeiculoService veiculoService, ClienteService clienteService, LocacaoService locacaoService) {
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;
        this.locacaoService = locacaoService;
    }


    @GetMapping("/")
    public String index(Model model) {
        // Obter métricas
        long frotaTotal = veiculoService.listarTodos().size();
        long veiculosDisponiveis = veiculoService.contarPorStatus(StatusVeiculo.DISPONIVEL);
        long veiculosLocados = veiculoService.contarPorStatus(StatusVeiculo.LOCADO);
        long emManutencao = veiculoService.contarPorStatus(StatusVeiculo.EM_MANUTENCAO);
        long inadimplentesTotal = clienteService.contarInadimplentes();
        long ativosTotal = locacaoService.contarPorStatus(StatusLocacao.ATIVA);

        // Obter locações recentes (ordenadas por id desc, limitado a 10)
        List<Locacao> recentesLocacoes = locacaoService.listarTodas().stream()
                .sorted((l1, l2) -> l2.getIdLoc().compareTo(l1.getIdLoc()))
                .limit(10)
                .collect(Collectors.toList());

        // Adicionar atributos ao model
        model.addAttribute("frotaTotal", frotaTotal);
        model.addAttribute("veiculosDisponiveis", veiculosDisponiveis);
        model.addAttribute("veiculosLocados", veiculosLocados);
        model.addAttribute("emManutencao", emManutencao);
        model.addAttribute("inadimplentesTotal", inadimplentesTotal);
        model.addAttribute("ativosTotal", ativosTotal);
        model.addAttribute("recentesLocacoes", recentesLocacoes);

        model.addAttribute("title", "Painel Operacional | RentFlow");
        model.addAttribute("activePage", "dashboard");

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
