package com.example.eixo.ordemservico.api.dto;

import com.example.eixo.ordemservico.model.MotivoCancelamentoOrdemServico;
import jakarta.validation.constraints.NotNull;

public record CancelarOrdemServicoRequest(
        @NotNull
        MotivoCancelamentoOrdemServico motivo
) {
}
