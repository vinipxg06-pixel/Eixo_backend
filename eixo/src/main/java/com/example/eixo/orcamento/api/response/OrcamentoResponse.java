package com.example.eixo.orcamento.api.response;

import com.example.eixo.orcamento.model.StatusOrcamento;

import java.math.BigDecimal;

public record OrcamentoResponse(
        Long orcamentoId,
        Long veiculoId,
        Long clienteId,
        StatusOrcamento status,
        BigDecimal valorTotal,
        String descricao
) {
}
