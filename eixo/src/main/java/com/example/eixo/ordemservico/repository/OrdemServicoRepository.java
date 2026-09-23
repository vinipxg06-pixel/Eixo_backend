package com.example.eixo.ordemservico.repository;

import com.example.eixo.ordemservico.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findAllByOficina_OficinaId(Long oficinaId);

    Optional<OrdemServico> findByIdOrdemServicoAndOficina_OficinaId(Long idOrdemServico, Long oficinaId);

    Optional<OrdemServico> findByOrcamento_IdOrcamento(Long idOrcamento);

    boolean existsByOrcamento_IdOrcamento(Long idOrcamento);
}