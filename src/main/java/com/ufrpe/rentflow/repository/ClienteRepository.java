package com.ufrpe.rentflow.repository;

import com.ufrpe.rentflow.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    long countByInadimplente(Boolean inadimplente);

    @org.springframework.data.jpa.repository.Query("SELECT c FROM Cliente c WHERE " +
           "(:filtrarBusca = false OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR c.cpf LIKE CONCAT('%', :busca, '%')) AND " +
           "(:filtrarInad = false OR c.inadimplente = :inadimplente)")
    java.util.List<Cliente> filtrarClientes(@org.springframework.data.repository.query.Param("busca") String busca, 
                                            @org.springframework.data.repository.query.Param("filtrarBusca") boolean filtrarBusca,
                                            @org.springframework.data.repository.query.Param("inadimplente") Boolean inadimplente,
                                            @org.springframework.data.repository.query.Param("filtrarInad") boolean filtrarInad);
}
