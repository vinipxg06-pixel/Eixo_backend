package com.example.eixo.pecasestoque.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PecaEstoqueAdicionarRequest(
        @NotNull
        @Positive
        BigDecimal quantidadeEntrada,

        @NotNull
        @PositiveOrZero
        BigDecimal precoUnitarioEntrada,
        /** Se true, a compra é lançada como saída no fluxo de caixa. */
        @NotNull
        Boolean registrarMovimentacao
) {
}
