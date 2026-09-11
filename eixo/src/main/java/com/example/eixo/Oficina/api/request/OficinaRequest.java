package com.example.eixo.Oficina.api.request;

import jakarta.validation.constraints.NotBlank;

public record OficinaRequest(
        @NotBlank
        String nomeOficina,
        @NotBlank
        String email,
        @NotBlank
        String telefone
) {
}
