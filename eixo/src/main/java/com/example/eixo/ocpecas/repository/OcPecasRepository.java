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

    Optional<OcPecas> findByIdOcPecasAndOrcamento_IdOrcamento(Long idOcPecas, Long idOrcamento);

    Optional<OcPecas> findByOrcamento_IdOrcamentoAndPecaEstoque_EstoqueIdAndValor(Long idOrcamento, Long estoqueId, BigDecimal valor);

    @Query("""
        select sum(i.valor * i.quantidade)
        from OcPecas i
        where i.orcamento.idOrcamento = :orcamentoId
    """)
    BigDecimal somarItens(@Param("orcamentoId") Long orcamentoId);

    boolean existsByPecaEstoque_EstoqueId(Long estoqueId);
}
