package com.example.eixo.ospecas.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OsPecasRequest(

        @NotNull
        Long estoqueId,

        @NotNull
        @Positive
        BigDecimal quantidade

) {
}
