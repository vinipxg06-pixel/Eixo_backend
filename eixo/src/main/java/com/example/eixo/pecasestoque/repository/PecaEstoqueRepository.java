package com.example.eixo.pecasestoque.repository;

import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PecaEstoqueRepository extends JpaRepository<PecaEstoque, Long> {

    List<PecaEstoque> findAllByOficina_OficinaId(Long OficinaId);
    PecaEstoque findByEstoqueIdAndOficina_OficinaId(Long estoqueId, Long oficinaId);
}
