package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNomeIgnoreCase(String nome);
}
