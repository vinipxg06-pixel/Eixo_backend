package com.example.eixo.usuarios.api.dto;

import com.example.eixo.usuarios.model.UsuarioStatus;

public record UsuarioResponse(
        Long idUsuario,
        String nomeUsuario,
        UsuarioStatus usuarioStatus,
        Long oficinaId
    ) {
}
