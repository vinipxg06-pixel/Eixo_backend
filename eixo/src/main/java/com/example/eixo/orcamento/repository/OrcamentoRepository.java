package com.example.eixo.orcamento.repository;

import com.example.eixo.orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findAllByOficina_oficinaId(Long oficinaId);
    Optional<Orcamento> findByIdOrcamentoAndOficina_oficinaId(Long idOrcamento, Long oficinaId);

}
