package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
    List<Veiculo> findByStatus(StatusVeiculo status);
    boolean existsByRenavam(String renavam);

    @org.springframework.data.jpa.repository.Query("SELECT v FROM Veiculo v WHERE " +
           "(:filtrarBusca = false OR LOWER(v.marca) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(v.modelo) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " +
           "(:filtrarStatus = false OR v.status = :status)")
    List<Veiculo> filtrarVeiculos(@org.springframework.data.repository.query.Param("busca") String busca, 
                                  @org.springframework.data.repository.query.Param("filtrarBusca") boolean filtrarBusca,
                                  @org.springframework.data.repository.query.Param("status") StatusVeiculo status,
                                  @org.springframework.data.repository.query.Param("filtrarStatus") boolean filtrarStatus);
}
