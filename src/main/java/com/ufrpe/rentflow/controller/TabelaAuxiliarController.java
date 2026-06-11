package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Categoria;
import com.ufrpe.rentflow.model.entity.Seguro;
import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.service.CategoriaService;
import com.ufrpe.rentflow.service.SeguroService;
import com.ufrpe.rentflow.service.FuncionarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tabelas-auxiliares")
public class TabelaAuxiliarController {

    private final CategoriaService categoriaService;
    private final SeguroService seguroService;
    private final FuncionarioService funcionarioService;

    public TabelaAuxiliarController(CategoriaService categoriaService, 
                                     SeguroService seguroService, 
                                     FuncionarioService funcionarioService) {
        this.categoriaService = categoriaService;
        this.seguroService = seguroService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public String exibirTabelas(@RequestParam(value = "tab", defaultValue = "categorias") String tab, Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("novaCategoria", new Categoria());
        
        model.addAttribute("seguros", seguroService.listarTodos());
        model.addAttribute("novoSeguro", new Seguro());
        
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        model.addAttribute("novoFuncionario", new Funcionario());

        model.addAttribute("activeTab", tab);
        model.addAttribute("title", "Tabelas Auxiliares | RentFlow");
        model.addAttribute("activePage", "auxiliares");
        return "tabelas/auxiliares";
    }

    @PostMapping("/categorias")
    public String salvarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.salvar(categoria);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar categoria: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=categorias";
    }

    @GetMapping("/categorias/excluir/{id}")
    public String excluirCategoria(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir categoria: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=categorias";
    }

    @PostMapping("/seguros")
    public String salvarSeguro(@ModelAttribute Seguro seguro, RedirectAttributes redirectAttributes) {
        try {
            seguroService.salvar(seguro);
            redirectAttributes.addFlashAttribute("successMessage", "Seguro salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar seguro: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=seguros";
    }

    @GetMapping("/seguros/excluir/{id}")
    public String excluirSeguro(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            seguroService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Seguro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir seguro: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=seguros";
    }

    @PostMapping("/funcionarios")
    public String salvarFuncionario(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.salvar(funcionario);
            redirectAttributes.addFlashAttribute("successMessage", "Funcionário salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar funcionário: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=funcionarios";
    }

    @GetMapping("/funcionarios/excluir/{id}")
    public String excluirFuncionario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Funcionário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir funcionário: " + e.getMessage());
        }
        return "redirect:/tabelas-auxiliares?tab=funcionarios";
    }

    @GetMapping("/funcionarios/detalhes/{id}")
    public String obterDetalhesFuncionario(@PathVariable("id") Integer id, Model model) {
        Funcionario funcionario = funcionarioService.listarTodos().stream()
                .filter(f -> f.getIdFunc().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));
        model.addAttribute("funcionario", funcionario);
        return "tabelas/funcionarios-detalhes";
    }
}
