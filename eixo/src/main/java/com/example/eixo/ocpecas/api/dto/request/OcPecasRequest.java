package com.example.eixo.ocpecas.api.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OcPecasRequest(
        @NotNull
        @Digits(integer = 10, fraction = 2)
        @Positive
        BigDecimal quantidade
) {
}
