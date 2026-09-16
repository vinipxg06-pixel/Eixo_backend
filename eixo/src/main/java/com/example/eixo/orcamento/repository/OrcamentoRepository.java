package com.example.eixo.orcamento.repository;

import com.example.eixo.orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
}
