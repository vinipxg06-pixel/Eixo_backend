package com.example.eixo.veiculo.api.dto;

import com.example.eixo.marcasmodelos.api.dtos.ModeloResponse;
import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;

public record VeiculoResponse(
        Long idVeiculo,
        String placa,
        Combustivel combustivel,
        Cor cor,
        String ano,
        Long quilometragem,
        Long oficinaId,
        ModeloResponse modelo,
        String nomeCliente
) {
}
