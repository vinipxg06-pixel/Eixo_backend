package com.example.eixo.ordemservico.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Quantidade de uma peça que foi realmente usada no serviço, informada no fechamento da OS.
 * Vale para a peça inteira (todas as linhas/preços dela). Zero = nenhuma unidade foi usada.
 */
public record PecaUtilizadaRequest(
        @NotNull
        Long estoqueId,
        @NotNull
        @PositiveOrZero
        @Digits(integer = 8, fraction = 2)
        BigDecimal quantidadeUtilizada
) {
}
