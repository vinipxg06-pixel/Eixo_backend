package com.example.eixo.oficina.api.response;

import java.time.LocalDateTime;

public record OficinaResponse(
        Long id,
        String nomeOficina,
        String email,
        String telefone,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}
