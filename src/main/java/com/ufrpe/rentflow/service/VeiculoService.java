package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Serviço para Veículos — garante a separação Controller → Service → Repository
@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    // Injeção de Dependências via Construtor (POO: Composição + Encapsulamento)
    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    public List<Veiculo> listarPorStatus(StatusVeiculo status) {
        return veiculoRepository.findByStatus(status);
    }

    public long contarPorStatus(StatusVeiculo status) {
        return veiculoRepository.findByStatus(status).size();
    }

    @org.springframework.transaction.annotation.Transactional
    public Veiculo salvar(Veiculo veiculo, boolean isNovo) {
        if (isNovo && veiculoRepository.existsById(veiculo.getPlaca())) {
            throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um veículo cadastrado com esta placa.");
        }
        
        if (veiculo.getRenavam() != null && !veiculo.getRenavam().trim().isEmpty()) {
            boolean renavamExiste = veiculoRepository.existsByRenavam(veiculo.getRenavam());
            if (renavamExiste) {
                if (isNovo) {
                    throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um veículo cadastrado com este RENAVAM.");
                } else {
                    java.util.Optional<Veiculo> outroOpt = veiculoRepository.findAll().stream()
                            .filter(v -> v.getRenavam().equals(veiculo.getRenavam()))
                            .findFirst();
                    if (outroOpt.isPresent() && !outroOpt.get().getPlaca().equals(veiculo.getPlaca())) {
                        throw new com.ufrpe.rentflow.exception.RegraNegocioException("Este RENAVAM já está associado a outro veículo.");
                    }
                }
            }
        }
        return veiculoRepository.save(veiculo);
    }

    @org.springframework.transaction.annotation.Transactional
    public void excluir(String placa) {
        veiculoRepository.deleteById(placa);
    }

    public List<Veiculo> filtrar(String busca, StatusVeiculo status) {
        String buscaParam = (busca == null) ? "" : busca;
        boolean filtrarBusca = busca != null && !busca.trim().isEmpty();
        boolean filtrarStatus = (status != null);
        StatusVeiculo statusParam = (status != null) ? status : StatusVeiculo.DISPONIVEL;
        return veiculoRepository.filtrarVeiculos(buscaParam, filtrarBusca, statusParam, filtrarStatus);
    }
}
