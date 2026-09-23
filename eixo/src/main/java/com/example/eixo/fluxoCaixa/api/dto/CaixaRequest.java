package com.example.eixo.fluxoCaixa.api.dto;

import com.example.eixo.fluxoCaixa.model.Categoria;
import com.example.eixo.fluxoCaixa.model.Tipo;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CaixaRequest(

    @NotBlank
    String descricao,

    @NotBlank
    Tipo tipo,

    @NotBlank
    BigDecimal valor,

    @NotBlank
    Categoria categoria

){
}
