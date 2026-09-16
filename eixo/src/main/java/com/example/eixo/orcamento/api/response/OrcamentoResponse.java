package com.example.eixo.orcamento.api.response;

import com.example.eixo.orcamento.model.StatusOrcamento;

public record OrcamentoResponse(

        Long orcamentoId,
        Long veiculoId,
        Long clienteId,
        StatusOrcamento status,
        double valorTotal,
        String descricao
) {
}
