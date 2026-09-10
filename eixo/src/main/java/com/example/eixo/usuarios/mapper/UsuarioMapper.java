package com.example.eixo.usuarios.mapper;

import com.example.eixo.usuarios.api.dto.UsuarioRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioRequest.email());
        usuario.setNomeUsuario(usuarioRequest.nomeUsuario());
        usuario.setSenha(usuarioRequest.senha());
        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario){
        return new UsuarioResponse(usuario.getIdUsuario(),
                usuario.getNomeUsuario(),
                usuario.getStatus());
    }
}
