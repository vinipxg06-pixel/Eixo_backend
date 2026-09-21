package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.model.UsuarioStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateRequest(
        @NotBlank
        String email,
        @NotBlank
        String nomeUsuario,
        @NotBlank
        String senha,
        @NotNull
        UsuarioStatus usuarioStatus
) {
}
