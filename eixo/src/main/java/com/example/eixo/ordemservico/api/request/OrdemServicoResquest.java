package com.example.eixo.ordemservico.api.request;

import java.math.BigDecimal;

public record OrdemServicoResquest(
        BigDecimal valorTotal,
        BigDecimal maoObra
) {
}
