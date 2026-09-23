package com.example.eixo.ordemservico.api.response;

import com.example.eixo.ordemservico.model.StatusOS;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrdemServicoResponse(
        Long ordemServicoId,
        StatusOS status,
        BigDecimal valorTotal,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        BigDecimal maoObra
) {
}
