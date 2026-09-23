package com.example.eixo.ospecas.api.dto;

import java.math.BigDecimal;

public record OsPecasResponse(

        Long idOsPecas,
        Long estoqueId,
        String nomePeca,
        BigDecimal valor,
        BigDecimal quantidade,
        BigDecimal valorTotal

) {
}