package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeguroRepository extends JpaRepository<Seguro, Integer> {
    boolean existsByNomeIgnoreCase(String nome);
}
