package com.example.eixo.ocpecas.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/** Adiciona uma peça ao orçamento (mesmo formato de OsPecasRequest). */
public record OcPecasRequest(
        @NotNull
        Long estoqueId,
        @NotNull
        @Digits(integer = 10, fraction = 2)
        @Positive
        BigDecimal quantidade
) {
}
