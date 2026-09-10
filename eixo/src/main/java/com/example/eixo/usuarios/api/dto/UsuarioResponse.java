package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.model.Status;

public record UsuarioResponse(
        Long idUsuario,
        String nomeUsuario,
        Status status
    ) {
}
