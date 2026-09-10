package com.example.eixo.Oficina.api.response;


import java.time.LocalDate;

public record OficinaResponse(
        Long id,
        String nomeOficina,
        String email,
        String telefone,
        LocalDate updatedAt,
        LocalDate createdAt

) {
}
