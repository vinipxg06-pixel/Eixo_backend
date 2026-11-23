package com.example.eixo.orcamento.repository;

import com.example.eixo.orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    @Query("select e from Orcamento e where e.oficina.oficinaId = ?1 order by e.createdAt desc, e.idOrcamento desc")
    List<Orcamento> findAllByOficina_oficinaId(Long oficinaId);
    Optional<Orcamento> findByIdOrcamentoAndOficina_oficinaId(Long idOrcamento, Long oficinaId);

    boolean existsByVeiculo_IdVeiculo(Long idVeiculo);

    boolean existsByCliente_ClienteId(Long clienteId);
}
