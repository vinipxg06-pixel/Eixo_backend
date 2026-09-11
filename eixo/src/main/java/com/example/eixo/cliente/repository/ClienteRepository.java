package com.example.eixo.cliente.repository;

import com.example.eixo.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByOficina_oficinaId(Long oficinaId);
    Optional<Cliente> findByClienteIdAndOficina_oficinaId(Long clienteId, Long oficinaId);
}
