package com.example.eixo.orcamento.api.request;

import com.example.eixo.orcamento.model.StatusOrcamento;
import jakarta.validation.constraints.NotNull;

public record OrcamentoStatusRequest(
        @NotNull
        StatusOrcamento statusOrcamento
) {
}
