package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Categoria;
import com.ufrpe.rentflow.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        if (categoria.getNome() != null && !categoria.getNome().trim().isEmpty()) {
            boolean existe = categoriaRepository.existsByNomeIgnoreCase(categoria.getNome());
            if (existe) {
                if (categoria.getIdCat() == null) {
                    throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe uma categoria cadastrada com este nome.");
                } else {
                    java.util.Optional<Categoria> outraOpt = categoriaRepository.findAll().stream()
                            .filter(c -> c.getNome().equalsIgnoreCase(categoria.getNome()))
                            .findFirst();
                    if (outraOpt.isPresent() && !outraOpt.get().getIdCat().equals(categoria.getIdCat())) {
                        throw new com.ufrpe.rentflow.exception.RegraNegocioException("Já existe outra categoria com este nome.");
                    }
                }
            }
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
