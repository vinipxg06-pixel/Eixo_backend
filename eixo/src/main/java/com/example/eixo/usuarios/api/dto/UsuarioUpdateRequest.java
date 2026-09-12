package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.model.UsuarioStatus;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateRequest(
        @NotBlank
        String email,
        @NotBlank
        String nomeUsuario,
        @NotBlank
        String senha,
        UsuarioStatus usuarioStatus
) {
}
