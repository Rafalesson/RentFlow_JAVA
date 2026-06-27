package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Vistoria;
import com.ufrpe.rentflow.model.enums.TipoVistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VistoriaRepository
        extends JpaRepository<Vistoria, Integer> {

    /**
     * Busca todas as vistorias de uma locação,
     * ordenadas da mais antiga para a mais recente.
     */
    List<Vistoria> findByLocacao_IdLocOrderByDataHoraAsc(
            Integer idLocacao
    );

    /**
     * Busca uma vistoria específica, como retirada ou devolução.
     */
    Optional<Vistoria> findByLocacao_IdLocAndTipo(
            Integer idLocacao,
            TipoVistoria tipo
    );

    /**
     * Verifica se determinada vistoria já foi registrada.
     */
    boolean existsByLocacao_IdLocAndTipo(
            Integer idLocacao,
            TipoVistoria tipo
    );

    /**
     * Busca todo o histórico de vistorias de um veículo.
     * Será utilizado futuramente no relatório de consumo.
     */
    List<Vistoria> findByLocacao_Veiculo_PlacaOrderByDataHoraAsc(
            String placa
    );
}