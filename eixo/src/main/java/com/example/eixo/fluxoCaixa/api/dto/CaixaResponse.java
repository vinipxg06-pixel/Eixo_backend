package com.example.eixo.fluxoCaixa.api.dto;

import com.example.eixo.fluxoCaixa.model.Categoria;
import com.example.eixo.fluxoCaixa.model.Tipo;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CaixaResponse(

        Long idFluxoCaixa,
        String descricao,
        Tipo tipo,
        BigDecimal valor,
        Categoria categoria,
        LocalDate dataMovimentacao,
        Byte osVinculada,
        PecaEstoque pecaEstoque,
        Oficina oficina
) {
}
