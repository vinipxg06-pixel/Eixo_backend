package com.example.eixo.marcasmodelos.api.dto;

public record ModeloResponse(
        Long idModelo,
        String nomeModelo,
        MarcaResponse marca
) {
}
