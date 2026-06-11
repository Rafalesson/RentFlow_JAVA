package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Manutencao;
import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.enums.TipoManutencao;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.service.ManutencaoService;
import com.ufrpe.rentflow.service.VeiculoService;
import com.ufrpe.rentflow.service.FuncionarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/manutencoes")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;
    private final VeiculoService veiculoService;
    private final FuncionarioService funcionarioService;

    public ManutencaoController(ManutencaoService manutencaoService, 
                                VeiculoService veiculoService, 
                                FuncionarioService funcionarioService) {
        this.manutencaoService = manutencaoService;
        this.veiculoService = veiculoService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public String listarManutencoes(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "tipo", required = false) TipoManutencao tipo,
            Model model) {
            
        model.addAttribute("manutencoes", manutencaoService.filtrar(busca, status, tipo));
        
        // Apenas veículos disponíveis ou locados podem ser enviados para manutenção
        model.addAttribute("veiculosDisponiveis", veiculoService.listarTodos().stream()
                .filter(v -> v.getStatus() == StatusVeiculo.DISPONIVEL || v.getStatus() == StatusVeiculo.LOCADO)
                .toList());
                
        model.addAttribute("tiposManutencao", TipoManutencao.values());
        model.addAttribute("title", "Manutenções | RentFlow");
        model.addAttribute("activePage", "manutencoes");
        return "manutencoes/lista";
    }

    @PostMapping("/salvar")
    public String salvarManutencao(
            Principal principal,
            @RequestParam(value = "placaVeiculo", required = false) String placaVeiculo,
            @RequestParam(value = "tipo", required = false) String tipoStr,
            @RequestParam(value = "motivo", required = false) String motivo,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "previsaoSaida", required = false) String previsaoSaidaStr,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            if (placaVeiculo == null || tipoStr == null || motivo == null || descricao == null || previsaoSaidaStr == null) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }

            Veiculo veiculo = veiculoService.listarTodos().stream()
                    .filter(v -> v.getPlaca().equals(placaVeiculo))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado."));

            Funcionario logado = funcionarioService.obterPorLogin(principal.getName());

            Manutencao manutencao = new Manutencao();
            manutencao.setVeiculo(veiculo);
            manutencao.setFuncionario(logado);
            manutencao.setTipo(TipoManutencao.valueOf(tipoStr.toUpperCase()));
            manutencao.setMotivo(motivo);
            manutencao.setDescricao(descricao);
            manutencao.setDataEntrada(LocalDate.now());
            manutencao.setPrevisaoSaida(LocalDate.parse(previsaoSaidaStr));

            manutencaoService.salvar(manutencao);
            redirectAttributes.addFlashAttribute("successMessage", "Veículo enviado para manutenção com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao registrar manutenção: " + e.getMessage());
        }

        return "redirect:/manutencoes";
    }

    @PostMapping("/concluir/{id}")
    public String concluirManutencao(
            @PathVariable("id") Integer id,
            @RequestParam(value = "custo", required = false) BigDecimal custo,
            RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.concluir(id, custo);
            redirectAttributes.addFlashAttribute("successMessage", "Manutenção concluída com sucesso! Veículo liberado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao concluir manutenção: " + e.getMessage());
        }
        return "redirect:/manutencoes";
    }

    @GetMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Registro de manutenção removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover manutenção: " + e.getMessage());
        }
        return "redirect:/manutencoes";
    }

    @GetMapping("/detalhes/{id}")
    public String obterDetalhes(@PathVariable("id") Integer id, Model model) {
        Manutencao manutencao = manutencaoService.listarTodas().stream()
                .filter(m -> m.getIdManut().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada."));
        model.addAttribute("manutencao", manutencao);
        return "manutencoes/detalhes";
    }
}
