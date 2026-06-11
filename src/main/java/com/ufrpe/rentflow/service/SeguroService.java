package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Seguro;
import com.ufrpe.rentflow.repository.SeguroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SeguroService {

    private final SeguroRepository seguroRepository;

    public SeguroService(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    public List<Seguro> listarTodos() {
        return seguroRepository.findAll();
    }

    @Transactional
    public Seguro salvar(Seguro seguro) {
        if (seguro.getNome() != null && !seguro.getNome().trim().isEmpty()) {
            boolean existe = seguroRepository.existsByNomeIgnoreCase(seguro.getNome());
            if (existe) {
                if (seguro.getIdSeguro() == null) {
                    throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe um seguro cadastrado com este nome.");
                } else {
                    java.util.Optional<Seguro> outroOpt = seguroRepository.findAll().stream()
                            .filter(s -> s.getNome().equalsIgnoreCase(seguro.getNome()))
                            .findFirst();
                    if (outroOpt.isPresent() && !outroOpt.get().getIdSeguro().equals(seguro.getIdSeguro())) {
                        throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe outro seguro com este nome.");
                    }
                }
            }
        }
        return seguroRepository.save(seguro);
    }

    @Transactional
    public void excluir(Integer id) {
        seguroRepository.deleteById(id);
    }
}
