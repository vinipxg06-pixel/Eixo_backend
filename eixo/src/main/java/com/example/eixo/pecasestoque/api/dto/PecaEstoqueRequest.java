package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PecaEstoqueRequest(
        @NotBlank
        String nomePeca,

        @NotBlank
        BigDecimal quantidade,

        @NotBlank
        Categoria categoria,

        String codigoBarras,

        @NotBlank
        BigDecimal estoqueMinimo,

        @NotBlank
        BigDecimal precoUnitario,

        @NotBlank
        UnidadeMedida unidadeMedida,

        @NotBlank
        Long oficinaId
) {
}
