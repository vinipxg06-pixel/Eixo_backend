package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PecaEstoqueRequest(
        @NotBlank
        String nomePeca,

        @NotNull
        @PositiveOrZero
        BigDecimal quantidade,

        @NotNull
        Categoria categoria,

        String codigoBarras,

        @NotNull
        @PositiveOrZero
        BigDecimal estoqueMinimo,

        @NotNull
        @PositiveOrZero
        BigDecimal precoUnitario,

        @NotNull
        UnidadeMedida unidadeMedida
) {
}
