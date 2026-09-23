package com.example.eixo.marcasmodelos.api.dtos;

public record ModeloResponse(
        Long idModelo,
        String nomeModelo,
        MarcaResponse marca
) {
}
