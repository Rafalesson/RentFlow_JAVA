package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocacaoRepository extends JpaRepository<Locacao, Integer> {
    List<Locacao> findByStatus(StatusLocacao status);
    List<Locacao> findByCliente_Cpf(String cpf);

    @org.springframework.data.jpa.repository.Query("SELECT l FROM Locacao l WHERE " +
           "(:filtrarBusca = false OR " +
           "LOWER(l.cliente.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           "l.cliente.cpf LIKE CONCAT('%', :busca, '%') OR " +
           "LOWER(l.veiculo.placa) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " +
           "(:filtrarStatus = false OR l.status = :status)")
    List<Locacao> filtrarLocacoes(@org.springframework.data.repository.query.Param("busca") String busca, 
                                  @org.springframework.data.repository.query.Param("filtrarBusca") boolean filtrarBusca,
                                  @org.springframework.data.repository.query.Param("status") StatusLocacao status,
                                  @org.springframework.data.repository.query.Param("filtrarStatus") boolean filtrarStatus);
}
