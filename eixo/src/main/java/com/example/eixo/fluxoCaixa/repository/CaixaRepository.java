package com.example.eixo.fluxoCaixa.repository;

import com.example.eixo.fluxoCaixa.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaixaRepository extends JpaRepository <Caixa,Long>{
    List<Caixa> findAllyOficona_oficinaId(Long oficinaId);
}
