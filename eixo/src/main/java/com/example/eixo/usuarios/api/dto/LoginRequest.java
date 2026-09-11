package com.example.eixo.usuarios.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O email e obrigatorio")
        String email,
        @NotBlank(message = "Senha e obrigatoria")
        String senha
) {

}
