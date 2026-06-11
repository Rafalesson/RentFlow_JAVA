package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public long contarTodos() {
        return clienteRepository.count();
    }

    public long contarInadimplentes() {
        return clienteRepository.countByInadimplente(true);
    }

    @org.springframework.transaction.annotation.Transactional
    public Cliente salvar(Cliente cliente, boolean isNovo) {
        if (cliente.getDataNascimento() == null) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("A data de nascimento é obrigatória.");
        }
        if (cliente.getDataNascimento().isAfter(java.time.LocalDate.now())) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("A data de nascimento não pode ser no futuro.");
        }
        if (cliente.getDataNascimento().plusYears(18).isAfter(java.time.LocalDate.now())) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("O cliente deve ter pelo menos 18 anos de idade.");
        }

        if (cliente.getCnhValidade() == null) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("A data de validade da CNH é obrigatória.");
        }
        if (cliente.getCnhValidade().isBefore(java.time.LocalDate.now())) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("A CNH do cliente está vencida.");
        }

        if (cliente.getCnhNumero() == null || cliente.getCnhNumero().trim().isEmpty()) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("O número da CNH é obrigatório.");
        }
        String cnhNumeros = cliente.getCnhNumero().replaceAll("\\D", "");
        if (cnhNumeros.length() > 11) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("O número da CNH deve conter no máximo 11 dígitos.");
        }

        if (isNovo && clienteRepository.existsById(cliente.getCpf())) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um cliente cadastrado com este CPF.");
        }
        return clienteRepository.save(cliente);
    }

    @org.springframework.transaction.annotation.Transactional
    public void excluir(String cpf) {
        clienteRepository.deleteById(cpf);
    }

    public List<Cliente> filtrar(String busca, Boolean inadimplente) {
        String buscaParam = (busca == null) ? "" : busca;
        boolean filtrarBusca = busca != null && !busca.trim().isEmpty();
        boolean filtrarInad = (inadimplente != null);
        Boolean inadParam = (inadimplente != null) ? inadimplente : false;
        return clienteRepository.filtrarClientes(buscaParam, filtrarBusca, inadParam, filtrarInad);
    }
}
