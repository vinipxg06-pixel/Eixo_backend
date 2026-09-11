package com.example.eixo.oficina.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OficinaRequest(
        @NotBlank
        String nomeOficina,
        @NotBlank
        String cnpj,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefone
) {
}
