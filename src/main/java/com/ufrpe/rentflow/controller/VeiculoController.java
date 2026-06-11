package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.model.enums.TipoCombustivel;
import com.ufrpe.rentflow.service.VeiculoService;
import com.ufrpe.rentflow.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;
    private final CategoriaService categoriaService;

    public VeiculoController(VeiculoService veiculoService, CategoriaService categoriaService) {
        this.veiculoService = veiculoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listarVeiculos(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "status", required = false) StatusVeiculo status,
            Model model) {
            
        model.addAttribute("veiculos", veiculoService.filtrar(busca, status));
        model.addAttribute("novoVeiculo", new Veiculo());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("combustiveis", TipoCombustivel.values());
        model.addAttribute("statusList", StatusVeiculo.values());
        model.addAttribute("title", "Frota de Veículos | RentFlow");
        model.addAttribute("activePage", "veiculos");
        return "veiculos/lista";
    }

    @PostMapping("/salvar")
    public String salvarVeiculo(
            @ModelAttribute Veiculo veiculo,
            @RequestParam(value = "isNovo", required = false, defaultValue = "false") boolean isNovo,
            RedirectAttributes redirectAttributes) {
        try {
            veiculoService.salvar(veiculo, isNovo);
            redirectAttributes.addFlashAttribute("successMessage", "Veículo salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar veículo: " + e.getMessage());
        }
        return "redirect:/veiculos";
    }


    @GetMapping("/excluir/{placa}")
    public String excluirVeiculo(@PathVariable("placa") String placa, RedirectAttributes redirectAttributes) {
        try {
            veiculoService.excluir(placa);
            redirectAttributes.addFlashAttribute("successMessage", "Veículo excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir veículo: " + e.getMessage());
        }
        return "redirect:/veiculos";
    }

    @GetMapping("/detalhes/{placa}")
    public String obterDetalhes(@PathVariable("placa") String placa, Model model) {
        Veiculo veiculo = veiculoService.listarTodos().stream()
                .filter(v -> v.getPlaca().equals(placa))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado."));
        model.addAttribute("veiculo", veiculo);
        return "veiculos/detalhes";
    }
}
