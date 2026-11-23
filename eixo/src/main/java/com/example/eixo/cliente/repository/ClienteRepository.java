package com.example.eixo.cliente.repository;

import com.example.eixo.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select e from Cliente e where e.oficina.oficinaId = ?1 order by e.createdAt desc, e.clienteId desc")
    List<Cliente> findAllByOficina_oficinaId(Long oficinaId);
    Optional<Cliente> findByClienteIdAndOficina_oficinaId(Long clienteId, Long oficinaId);

    boolean existsByCpfCnpjAndOficina_OficinaId(String cpfCnpj, Long oficinaId);

    boolean existsByCpfCnpjAndOficina_OficinaIdAndClienteIdNot(String cpfCnpj, Long oficinaId, Long clienteId);
}
