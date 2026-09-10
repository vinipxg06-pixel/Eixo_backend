package com.example.eixo.usuarios.api.dto;


import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;

public record UsuarioRequest(
    @NotBlank
    String nomeUsuario,
    @NotBlank
    String email,
    @NotBlank
    String senha
){
}
