package com.example.eixo.pecasestoque.api.dto;

import com.example.eixo.pecasestoque.model.Categoria;
import com.example.eixo.pecasestoque.model.UnidadeMedida;

import java.math.BigDecimal;

public record PecaEstoqueResponse(
        Long estoqueId,
        String nomePeca,
        BigDecimal quantidade,
        Categoria categoria,
        String codigoBarras,
        BigDecimal estoqueMinimo,
        BigDecimal precoUnitario,
        UnidadeMedida unidadeMedida,
        Long oficinaId
) {
}
