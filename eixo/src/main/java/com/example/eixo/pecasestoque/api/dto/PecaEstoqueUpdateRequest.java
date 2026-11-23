package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Edição dos dados cadastrais de uma peça.
 * Quantidade e preço unitário NÃO fazem parte da edição:
 * - quantidade muda apenas por entrada/saída de estoque;
 * - preço unitário é o preço médio, recalculado a cada entrada.
 */
public record PecaEstoqueUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String nomePeca,
        @NotNull
        Categoria categoria,
        @Size(max = 100)
        String codigoBarras,
        @NotNull
        @PositiveOrZero
        BigDecimal estoqueMinimo,
        @NotNull
        UnidadeMedida unidadeMedida
) {
}
