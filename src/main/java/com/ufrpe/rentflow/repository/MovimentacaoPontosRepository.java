package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.MovimentacaoPontos;
import com.ufrpe.rentflow.model.enums.TipoMovimentacaoPontos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoPontosRepository
        extends JpaRepository<MovimentacaoPontos, Integer> {

    List<MovimentacaoPontos>
    findByCliente_CpfOrderByDataHoraDesc(String cpf);

    boolean existsByLocacao_IdLocAndTipo(
            Integer idLoc,
            TipoMovimentacaoPontos tipo
    );
}