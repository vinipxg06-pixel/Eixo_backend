package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.api.validacao.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
    @NotBlank(message = "O nome de usuario e obrigatorio")
    @Size(max = 100)
    String nomeUsuario,
    @Email
    @NotBlank(message = "O email e obrigatorio")
    @Size(max = 100)
    String email,
    @NotBlank(message = "Senha e obrigatoria")
    @SenhaForte
    @Size(max = 72, message = "A senha deve ter no máximo 72 caracteres")
    String senha
){
}
