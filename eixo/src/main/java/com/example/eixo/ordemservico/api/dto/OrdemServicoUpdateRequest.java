package com.example.eixo.ordemservico.api.dto;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record OrdemServicoUpdateRequest(

        @PositiveOrZero
        BigDecimal maoDeObra,

        @NotBlank
        @Size(max = 100)
        String descricao,
        /** Estado final das peças. Nulo = peças não são alteradas; lista vazia = remove todas. */
        @Valid
        List<LinhaPecaRequest> pecas
) {
}