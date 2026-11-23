package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PecaEstoqueRequest(
        @NotBlank
        @Size(max = 100)
        String nomePeca,

        @NotNull
        @PositiveOrZero
        BigDecimal quantidade,

        @NotNull
        Categoria categoria,

        @Size(max = 100)
        String codigoBarras,

        @NotNull
        @PositiveOrZero
        BigDecimal estoqueMinimo,

        @NotNull
        @PositiveOrZero
        BigDecimal precoUnitario,

        @NotNull
        UnidadeMedida unidadeMedida,
        /** Se true, a quantidade inicial é lançada como compra (saída) no fluxo de caixa. */
        @NotNull
        Boolean registrarMovimentacao
) {
}
