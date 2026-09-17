package com.example.eixo.orcamento.api.request;

import com.example.eixo.orcamento.model.StatusOrcamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrcamentoUpdateRequest(
        @NotNull
        StatusOrcamento statusOrcamento,

        BigDecimal maoDeObra,

        @NotNull
        @Positive
        BigDecimal valorTotal,

        String descricao
) {
}
