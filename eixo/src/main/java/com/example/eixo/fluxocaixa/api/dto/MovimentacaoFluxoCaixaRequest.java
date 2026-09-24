package com.example.eixo.fluxocaixa.api.dto;

import com.example.eixo.fluxocaixa.model.CategoriaMovimentacao;
import com.example.eixo.fluxocaixa.model.TipoMovimentacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MovimentacaoFluxoCaixaRequest(

        @NotBlank
        @Size(max = 100)
        String descricao,

        @NotNull
        TipoMovimentacao tipo,

        @NotNull
        @DecimalMin(value = "0.00", inclusive = true)
        BigDecimal valor,

        @NotNull
        CategoriaMovimentacao categoria
) {
}
