package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.api.validacao.SenhaForte;
import com.example.eixo.usuarios.model.UsuarioStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank
        @Email
        @Size(max = 100)
        String email,
        @NotBlank
        @Size(max = 100)
        String nomeUsuario,
        @NotBlank
        @SenhaForte
        @Size(max = 72, message = "A senha deve ter no máximo 72 caracteres")
        String senha,
        @NotNull
        UsuarioStatus usuarioStatus
) {
}
