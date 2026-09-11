package com.example.eixo.marcasmodelos.repository;

import com.example.eixo.marcasmodelos.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
