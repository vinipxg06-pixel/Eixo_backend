package com.example.eixo.pecasestoque.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PecaEstoqueRemoverRequest(
        @NotBlank
        BigDecimal quantidade
) {
}
