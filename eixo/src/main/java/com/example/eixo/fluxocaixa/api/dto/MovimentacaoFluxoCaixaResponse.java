package com.example.eixo.fluxocaixa.api.dto;

import com.example.eixo.fluxocaixa.model.CategoriaMovimentacao;
import com.example.eixo.fluxocaixa.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentacaoFluxoCaixaResponse(
        Long idMovimentacao,
        String descricao,
        TipoMovimentacao tipo,
        BigDecimal valor,
        CategoriaMovimentacao categoria,
        LocalDate dataMovimentacao,
        Boolean osVinculada,
        Long ordemServicoId,
        Long estoqueId,
        Long oficinaId
) {
}
