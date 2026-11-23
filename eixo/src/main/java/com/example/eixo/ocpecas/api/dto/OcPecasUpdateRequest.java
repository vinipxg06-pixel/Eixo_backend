package com.example.eixo.ocpecas.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/** Altera a quantidade de uma linha de peça do orçamento. */
public record OcPecasUpdateRequest(
        @NotNull
        @Digits(integer = 10, fraction = 2)
        @Positive
        BigDecimal quantidade
) {
}
