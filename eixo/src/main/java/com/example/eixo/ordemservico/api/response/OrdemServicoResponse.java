package com.example.eixo.ordemservico.api.response;

import com.example.eixo.ordemservico.model.StatusOS;

import java.time.LocalDateTime;

public record OrdemServicoResponse(
        Long ordemServicoId,
        StatusOS status,
        Double valorTotal,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        Double maoObra
) {
}
