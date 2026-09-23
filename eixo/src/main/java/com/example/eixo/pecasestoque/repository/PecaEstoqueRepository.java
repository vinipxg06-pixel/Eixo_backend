package com.example.eixo.pecasestoque.repository;

import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PecaEstoqueRepository extends JpaRepository<PecaEstoque, Long> {

    List<PecaEstoque> findAllByOficina_OficinaId(Long OficinaId);
    Optional<PecaEstoque> findByEstoqueIdAndOficina_OficinaId(Long estoqueId, Long oficinaId);
}
