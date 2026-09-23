package com.example.eixo.ordemservico.api.dto;

import com.example.eixo.ordemservico.model.StatusOrdemServico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrdemServicoResponse(

        Long idOrdemServico,
        StatusOrdemServico status,
        String descricao,
        BigDecimal valorTotal,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        BigDecimal maoDeObra,
        Long oficinaId,
        Long clienteId,
        Long veiculoId,
        Long orcamentoId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}