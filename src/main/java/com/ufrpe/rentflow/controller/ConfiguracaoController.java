package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.service.FuncionarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/configuracoes")
public class ConfiguracaoController {

    private final FuncionarioService funcionarioService;

    public ConfiguracaoController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public String exibirPerfil(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        Funcionario func = funcionarioService.obterPorLogin(principal.getName());
        model.addAttribute("funcionario", func);
        model.addAttribute("title", "Configurações | RentFlow");
        model.addAttribute("activePage", "configuracoes");
        return "configuracoes/perfil";
    }

    @PostMapping("/salvar")
    public String salvarPerfil(
            Principal principal,
            @RequestParam("nome") String nome,
            @RequestParam("cpf") String cpf,
            @RequestParam("email") String email,
            @RequestParam(value = "fotoPerfil", required = false) String fotoPerfil,
            @RequestParam(value = "senhaAntiga", required = false) String senhaAntiga,
            @RequestParam(value = "novaSenha", required = false) String novaSenha,
            @RequestParam(value = "confirmacaoSenha", required = false) String confirmacaoSenha,
            RedirectAttributes redirectAttributes) {
            
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String senhaParaAtualizar = null;
            // Se o usuário tentou preencher qualquer campo de senha
            if ((novaSenha != null && !novaSenha.trim().isEmpty()) || 
                (confirmacaoSenha != null && !confirmacaoSenha.trim().isEmpty()) ||
                (senhaAntiga != null && !senhaAntiga.trim().isEmpty())) {
                
                if (senhaAntiga == null || senhaAntiga.trim().isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A senha atual é obrigatória para alterar a senha.");
                    return "redirect:/configuracoes";
                }
                if (novaSenha == null || novaSenha.trim().isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A nova senha não pode ser vazia.");
                    return "redirect:/configuracoes";
                }
                if (!novaSenha.equals(confirmacaoSenha)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A nova senha e a confirmação não coincidem.");
                    return "redirect:/configuracoes";
                }
                if (!funcionarioService.verificarSenha(principal.getName(), senhaAntiga)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A senha atual informada está incorreta.");
                    return "redirect:/configuracoes";
                }
                senhaParaAtualizar = novaSenha;
            }

            funcionarioService.atualizarPerfil(principal.getName(), nome, cpf, email, senhaParaAtualizar, fotoPerfil);
            if (senhaParaAtualizar != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Configurações e senha atualizadas com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Configurações de perfil atualizadas com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar perfil: " + e.getMessage());
        }

        return "redirect:/configuracoes";
    }
}
