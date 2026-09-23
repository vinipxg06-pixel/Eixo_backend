package com.example.eixo.ocpecas.api.dto.response;

import java.math.BigDecimal;

public record OcPecasResponse(
        Long idOcPecas,
        Long estoqueId,
        String nomePeca,
        BigDecimal valor,
        BigDecimal quantidade,
        BigDecimal subtotal
) {
}
