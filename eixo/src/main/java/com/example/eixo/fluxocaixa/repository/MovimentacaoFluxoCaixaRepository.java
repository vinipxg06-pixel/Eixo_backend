package com.example.eixo.fluxocaixa.repository;

import com.example.eixo.fluxocaixa.model.MovimentacaoFluxoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoFluxoCaixaRepository extends JpaRepository<MovimentacaoFluxoCaixa, Long> {

    @Query("select e from MovimentacaoFluxoCaixa e where e.oficina.oficinaId = ?1 order by e.idMovimentacao desc")
    List<MovimentacaoFluxoCaixa> findAllByOficina_OficinaId(Long oficinaId);

    Optional<MovimentacaoFluxoCaixa> findByIdMovimentacaoAndOficina_OficinaId(
            Long idMovimentacao,
            Long oficinaId
    );

    boolean existsByOrdemServico_IdOrdemServico(Long ordemServicoId);

    boolean existsByPecaEstoque_EstoqueId(Long estoqueId);
}
