package com.example.eixo.orcamento.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrcamentoUpdateRequest(

        @PositiveOrZero
        BigDecimal maoDeObra,

        @NotBlank
        @Size(max = 100)
        String descricao

) {
}