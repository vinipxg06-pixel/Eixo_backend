package com.example.eixo.orcamento.api.request;

import com.example.eixo.orcamento.model.StatusOrcamento;
import jakarta.validation.constraints.NotBlank;

public record OrcamentoStatusRequest(
        @NotBlank
        Long clienteId,
        @NotBlank
        Long oficinaId,
        @NotBlank
        Long id,
        @NotBlank
        double maoDeObra,
        @NotBlank
        double valorTotal,
        StatusOrcamento status,
        String descricao

) {
}
