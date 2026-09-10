package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.model.Status;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateRequest(
        @NotBlank
        String email,
        @NotBlank
        String senha,
        Status status
) {
}
