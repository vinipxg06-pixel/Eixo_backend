package com.example.eixo.ospecas.repository;

import com.example.eixo.ospecas.model.OsPecas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OsPecasRepository extends JpaRepository<OsPecas, Long> {

    List<OsPecas> findAllByOrdemServico_IdOrdemServico(Long idOrdemServico);

    Optional<OsPecas> findByIdOsPecasAndOrdemServico_IdOrdemServico(Long idOsPecas, Long idOrdemServico);

    Optional<OsPecas> findByOrdemServico_IdOrdemServicoAndPecaEstoque_EstoqueId(Long idOrdemServico, Long estoqueId);

    @Query("""
            SELECT SUM(op.valor * op.quantidade)
            FROM OsPecas op
            WHERE op.ordemServico.idOrdemServico = :idOrdemServico
            """)
    BigDecimal somarItens(
            @Param("idOrdemServico") Long idOrdemServico
    );

    void deleteAllByOrdemServico_IdOrdemServico(Long idOrdemServico);
}