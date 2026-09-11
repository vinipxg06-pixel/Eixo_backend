package com.example.eixo.marcasModelos.api.dtos;

import com.example.eixo.marcasModelos.model.Marca;

public record ModeloResponse(
        Long idModelo,
        String nomeModelo,
        Marca marca
) {
}
