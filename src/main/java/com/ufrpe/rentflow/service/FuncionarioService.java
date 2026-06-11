package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.enums.CargoFuncionario;
import com.ufrpe.rentflow.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Funcionario obterPorLogin(String login) {
        Optional<Funcionario> opt = funcionarioRepository.findByLogin(login);
        if (opt.isPresent()) {
            return opt.get();
        }

        // Se o funcionário não existir no banco de dados (por exemplo, ao logar com as credenciais em memória
        // do SecurityConfig), nós o provisionamos automaticamente para evitar erros de banco de dados.
        Funcionario novo = new Funcionario();
        novo.setLogin(login);
        if ("admin".equals(login)) {
            novo.setNome("Administrador RentFlow");
            novo.setCpf("111.111.111-11");
            novo.setCargo(CargoFuncionario.GERENTE);
            novo.setEmail("admin@rentflow.com");
            novo.setSenha(passwordEncoder.encode("admin"));
        } else {
            novo.setNome("Atendente RentFlow");
            novo.setCpf("222.222.222-22");
            novo.setCargo(CargoFuncionario.ATENDENTE);
            novo.setEmail("atendente@rentflow.com");
            novo.setSenha(passwordEncoder.encode("123"));
        }
        return funcionarioRepository.save(novo);
    }

    @Transactional
    public Funcionario atualizarPerfil(String login, String nome, String cpf, String email, String novaSenha, String fotoPerfil) {
        Funcionario func = obterPorLogin(login);
        func.setNome(nome);
        func.setEmail(email);
        
        func.setFotoPerfil(fotoPerfil != null && !fotoPerfil.trim().isEmpty() ? fotoPerfil.trim() : null);

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            func.setSenha(passwordEncoder.encode(novaSenha.trim()));
        }

        return funcionarioRepository.save(func);
    }

    public boolean verificarSenha(String login, String senhaPura) {
        Funcionario func = obterPorLogin(login);
        if (func == null || func.getSenha() == null) {
            return false;
        }
        return passwordEncoder.matches(senhaPura, func.getSenha());
    }

    public java.util.List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        if (funcionario.getCpf() != null && !funcionario.getCpf().trim().isEmpty()) {
            boolean cpfExiste = funcionarioRepository.existsByCpf(funcionario.getCpf());
            if (cpfExiste) {
                if (funcionario.getIdFunc() == null) {
                    throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um funcionário cadastrado com este CPF.");
                } else {
                    Optional<Funcionario> outroOpt = funcionarioRepository.findByCpf(funcionario.getCpf());
                    if (outroOpt.isPresent() && !outroOpt.get().getIdFunc().equals(funcionario.getIdFunc())) {
                        throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe outro funcionário cadastrado com este CPF.");
                    }
                }
            }
        }
        
        if (funcionario.getLogin() != null && !funcionario.getLogin().trim().isEmpty()) {
            boolean loginExiste = funcionarioRepository.existsByLogin(funcionario.getLogin());
            if (loginExiste) {
                if (funcionario.getIdFunc() == null) {
                    throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um funcionário cadastrado com este Login.");
                } else {
                    Optional<Funcionario> outroOpt = funcionarioRepository.findByLogin(funcionario.getLogin());
                    if (outroOpt.isPresent() && !outroOpt.get().getIdFunc().equals(funcionario.getIdFunc())) {
                        throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe outro funcionário cadastrado com este Login.");
                    }
                }
            }
        }

        if (funcionario.getSenha() != null && !funcionario.getSenha().trim().isEmpty()) {
            funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha().trim()));
        }
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void excluir(Integer id) {
        funcionarioRepository.deleteById(id);
    }
}
