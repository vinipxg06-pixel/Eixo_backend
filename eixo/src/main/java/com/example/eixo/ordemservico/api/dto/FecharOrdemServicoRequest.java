package com.example.eixo.ordemservico.api.dto;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Confirmação do fechamento da OS.
 * pecas: ajustes de uso; peças que não vierem na lista são consideradas usadas por inteiro (nulo = nenhum ajuste).
 * confirmarSemCusto: obrigatório quando a OS fecha com valor zero.
 */
public record FecharOrdemServicoRequest(
        @Valid
        List<PecaUtilizadaRequest> pecas,
        boolean confirmarSemCusto
) {
}
