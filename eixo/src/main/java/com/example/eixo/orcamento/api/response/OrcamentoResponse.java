package com.example.eixo.orcamento.api.response;

import com.example.eixo.orcamento.model.StatusOrcamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoResponse(
        Long orcamentoId,
        Long clienteId,
        Long veiculoId,
        StatusOrcamento status,
        String descricao,
        BigDecimal maoDeObra,
        BigDecimal valorTotal,
        LocalDateTime createdAt
) {
}