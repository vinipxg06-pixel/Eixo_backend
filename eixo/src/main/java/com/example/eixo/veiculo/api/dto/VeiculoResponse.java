package com.example.eixo.veiculo.api.dto;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.marcasmodelos.model.Marca;
import com.example.eixo.marcasmodelos.model.Modelo;
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
        Modelo Modelo,
        String nomeCliente
) {
}
