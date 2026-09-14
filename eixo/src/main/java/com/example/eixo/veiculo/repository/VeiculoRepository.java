package com.example.eixo.veiculo.repository;

import com.example.eixo.veiculo.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface VeiculoRepository  extends JpaRepository<Veiculo, Long> {

}
