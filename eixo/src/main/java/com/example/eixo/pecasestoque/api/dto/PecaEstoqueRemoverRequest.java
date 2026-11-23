package com.example.eixo.pecasestoque.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PecaEstoqueRemoverRequest(
        @NotNull
        @Positive
        BigDecimal quantidade,
        /** Se true, a retirada é lançada como venda de peça (entrada) no fluxo de caixa. */
        @NotNull
        Boolean registrarMovimentacao,
        /** Valor unitário de venda; obrigatório quando registrarMovimentacao = true. */
        @PositiveOrZero
        BigDecimal valorUnitarioVenda
) {
}
