package com.example.eixo.oficina.api.dto;

import java.time.LocalDateTime;

public record OficinaResponse(
        Long oficinaId,
        String nomeOficina,
        String cnpj,
        String email,
        String telefone,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}
