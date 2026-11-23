package com.example.eixo.veiculo.repository;

import com.example.eixo.veiculo.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlacaAndOficina_OficinaId(String placa, Long oficinaId);

    @Query("select e from Veiculo e where e.oficina.oficinaId = ?1 order by e.createdAt desc, e.idVeiculo desc")
    List<Veiculo> findAllByOficina_OficinaId(Long oficinaId);

    Optional<Veiculo> findByIdVeiculoAndOficina_OficinaId(Long idVeiculo, Long oficinaId);

    @Query("select e from Veiculo e where e.cliente.clienteId = ?1 and e.oficina.oficinaId = ?2 order by e.createdAt desc, e.idVeiculo desc")
    List<Veiculo> findAllByCliente_ClienteIdAndOficina_OficinaId(Long clienteId, Long oficinaId);

    boolean existsByCliente_ClienteId(Long clienteId);
}
