package com.example.eixo.ordemservico.repository;

import com.example.eixo.ordemservico.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoRespository extends JpaRepository<OrdemServico, Long> {
    List<OrdemServico> findAllByOficina_oficinaId(Long oficinaId);
}
