package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PecaEstoqueRequest(
        @NotBlank
        String nomePeca,

        BigDecimal quantidade,

        Categoria categoria,

        String codigoBarras,

        BigDecimal estoqueMinimo,

        BigDecimal precoUnitario,

        UnidadeMedida unidadeMedida
) {
}
