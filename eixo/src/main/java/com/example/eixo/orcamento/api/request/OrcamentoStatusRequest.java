package com.example.eixo.orcamento.api.request;

import com.example.eixo.orcamento.model.StatusOrcamento;
import jakarta.validation.constraints.NotBlank;

public record OrcamentoStatusRequest(
        @NotBlank
        StatusOrcamento statusOrcamento
) {
}
