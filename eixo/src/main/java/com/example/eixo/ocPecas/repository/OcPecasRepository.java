package com.example.eixo.ocpecas.repository;

import com.example.eixo.ocpecas.model.OcPecas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OcPecasRepository extends JpaRepository<OcPecas, Long> {

    List<OcPecas> findAllByOrcamento_idOrcamento(Long idOrcamento);

    Optional<OcPecas> findByOrcamento_idOrcamentoAndPecaEstoque_estoqueId(Long idOrcamento, Long estoqueId);

    @Query("""
        select sum(i.valor * i.quantidade)
        from OcPecas i
        where i.orcamento.idOrcamento = :orcamentoId
    """)
    BigDecimal somarItens(@Param("orcamentoId") Long orcamentoId);
}