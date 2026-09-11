package com.example.eixo.marcasModelos.repository;

import com.example.eixo.marcasModelos.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
