package com.example.eixo.veiculo.repository;

import com.example.eixo.veiculo.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlaca(String placa);

    List<Veiculo> findByCliente_ClienteId(Long clienteId);

    List<Veiculo> findAllByOficina_OficinaId(Long oficinaId);

}
