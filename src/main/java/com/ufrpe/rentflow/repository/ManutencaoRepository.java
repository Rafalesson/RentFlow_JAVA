package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer> {
    @org.springframework.data.jpa.repository.Query("SELECT m FROM Manutencao m WHERE " +
           "(:filtrarBusca = false OR " +
           "LOWER(m.veiculo.placa) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           "LOWER(m.motivo) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           "LOWER(m.descricao) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " +
           "(:filtrarStatus = false OR " +
           "(m.dataSaidaReal IS NOT NULL AND :status = 'CONCLUIDA') OR " +
           "(m.dataSaidaReal IS NULL AND :status = 'EM_ANDAMENTO')) AND " +
           "(:filtrarTipo = false OR m.tipo = :tipo)")
    java.util.List<Manutencao> filtrarManutencoes(@org.springframework.data.repository.query.Param("busca") String busca, 
                                                  @org.springframework.data.repository.query.Param("filtrarBusca") boolean filtrarBusca,
                                                  @org.springframework.data.repository.query.Param("status") String status, 
                                                  @org.springframework.data.repository.query.Param("filtrarStatus") boolean filtrarStatus,
                                                  @org.springframework.data.repository.query.Param("tipo") com.ufrpe.rentflow.model.enums.TipoManutencao tipo,
                                                  @org.springframework.data.repository.query.Param("filtrarTipo") boolean filtrarTipo);
}
