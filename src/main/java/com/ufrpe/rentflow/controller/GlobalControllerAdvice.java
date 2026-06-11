package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.service.FuncionarioService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final FuncionarioService funcionarioService;

    public GlobalControllerAdvice(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @ModelAttribute("usuarioLogado")
    public Funcionario adicionarUsuarioLogado(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            try {
                return funcionarioService.obterPorLogin(authentication.getName());
            } catch (Exception e) {
                // Silently ignore DB errors during startup/auth to prevent page crash
            }
        }
        return null;
    }
}
