package com.example.eixo.fluxoCaixa.api.dto;

import com.example.eixo.fluxoCaixa.model.Categoria;
import com.example.eixo.fluxoCaixa.model.Tipo;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.pecasestoque.model.PecaEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaixaResponse(

        Long idFluxoCaixa,
        String descricao,
        Tipo tipo,
        BigDecimal valor,
        Categoria categoria,
        LocalDateTime dataMovimentacao,
        Integer osVinculada,
        PecaEstoque pecaEstoque,
        Oficina oficina
) {
}
