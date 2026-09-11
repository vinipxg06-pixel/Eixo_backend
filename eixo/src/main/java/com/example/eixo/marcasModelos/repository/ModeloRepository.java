package com.example.eixo.marcasModelos.repository;

import com.example.eixo.marcasModelos.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    List<Modelo> findAllByMarca_IdMarca(Long idMarca);

}
