package com.example.eixo.usuarios.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
    @NotBlank(message = "O nome de usuario e obrigatorio")
    String nomeUsuario,
    @Email
    @NotBlank(message = "O email e obrigatorio")
    String email,
    @NotBlank(message = "Senha e obrigatoria")
    String senha
){
}
