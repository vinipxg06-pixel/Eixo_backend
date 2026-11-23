package com.example.eixo.orcamento.api.dto;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/** Criação de orçamento. Cliente e veículo vão no corpo, não na URL. */
public record OrcamentoRequest(

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

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