package com.example.eixo.pecasestoque.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PecaEstoqueRemoverRequest(
        @NotNull
        @Positive
        BigDecimal quantidade
) {
}
