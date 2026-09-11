package com.example.eixo.usuarios.api.dto;


import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;

public record UsuarioRequest(
    @NotBlank(message = "O nome de usuario e obrigatorio")
    String nomeUsuario,
    @NotBlank(message = "O email e obrigatorio")
    String email,
    @NotBlank(message = "Senha e obrigatoria")
    String senha
){
}
