package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.model.enums.CategoriaCnh;
import com.ufrpe.rentflow.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "inadimplente", required = false) Boolean inadimplente,
            Model model) {
            
        model.addAttribute("clientes", clienteService.filtrar(busca, inadimplente));
        model.addAttribute("novoCliente", new Cliente());
        model.addAttribute("categoriasCnh", CategoriaCnh.values());
        model.addAttribute("title", "Clientes | RentFlow");
        model.addAttribute("activePage", "clientes");
        return "clientes/lista";
    }

    @PostMapping("/salvar")
    public String salvarCliente(
            @ModelAttribute Cliente cliente,
            @RequestParam(value = "isNovo", required = false, defaultValue = "false") boolean isNovo,
            RedirectAttributes redirectAttributes) {
        try {
            clienteService.salvar(cliente, isNovo);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }



    @GetMapping("/excluir/{cpf}")
    public String excluirCliente(@PathVariable("cpf") String cpf, RedirectAttributes redirectAttributes) {
        try {
            clienteService.excluir(cpf);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/detalhes/{cpf:.+}")
    public String obterDetalhes(@PathVariable("cpf") String cpf, Model model) {
        Cliente cliente = clienteService.listarTodos().stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        model.addAttribute("cliente", cliente);
        return "clientes/detalhes";
    }
}
