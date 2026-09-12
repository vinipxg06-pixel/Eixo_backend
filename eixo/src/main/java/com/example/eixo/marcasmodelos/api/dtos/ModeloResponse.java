package com.example.eixo.marcasmodelos.api.dtos;

import com.example.eixo.marcasmodelos.model.Marca;

public record ModeloResponse(
        Long idModelo,
        String nomeModelo,
        Marca marca
) {
}
