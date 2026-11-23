package com.example.eixo.pecasestoque.repository;

import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PecaEstoqueRepository extends JpaRepository<PecaEstoque, Long> {

    @Query("select e from PecaEstoque e where e.oficina.oficinaId = ?1 order by e.createdAt desc, e.estoqueId desc")
    List<PecaEstoque> findAllByOficina_OficinaId(Long OficinaId);
    Optional<PecaEstoque> findByEstoqueIdAndOficina_OficinaId(Long estoqueId, Long oficinaId);

    boolean existsByCodigoBarrasAndOficina_OficinaId(String codigoBarras, Long oficinaId);

    boolean existsByCodigoBarrasAndOficina_OficinaIdAndEstoqueIdNot(String codigoBarras, Long oficinaId, Long estoqueId);
}
