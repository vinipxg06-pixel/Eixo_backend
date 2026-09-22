package com.example.eixo.orcamento.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrcamentoRequest(

        BigDecimal maoDeObra,

        @NotNull
        @Positive
        BigDecimal valorTotal,

        String descricao

) {}
