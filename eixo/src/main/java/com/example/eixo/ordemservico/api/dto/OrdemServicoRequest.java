package com.example.eixo.ordemservico.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrdemServicoRequest(

        @PositiveOrZero
        BigDecimal maoDeObra,

        @NotBlank
        @Size(max = 100)
        String descricao

) {
}