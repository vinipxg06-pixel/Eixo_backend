package com.example.eixo.ospecas.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OsPecasUpdateRequest(

        @NotNull
        @Positive
        BigDecimal quantidade

) {
}
