package com.example.eixo.Oficina.api.response;

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
