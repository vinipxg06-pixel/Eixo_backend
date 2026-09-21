package com.example.eixo.ocPecas.repository;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.ocPecas.model.OcPecas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcPecasRepository extends JpaRepository<OcPecas, Long> {

    List<OcPecas> findAllByOrcamento_idOrcamento(Long idOrcamento);

}
