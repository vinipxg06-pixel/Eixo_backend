package com.example.eixo.usuarios.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.usuarios.api.dto.LoginResponse;
import com.example.eixo.usuarios.api.dto.UsuarioRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UsuarioMapper {

    // a senha chega em texto puro; o UsuarioService aplica o hash antes de salvar
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "usuarioStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    Usuario paraEntidade(UsuarioRequest usuarioRequest);

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    UsuarioResponse paraResposta(Usuario usuario);

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    LoginResponse paraRespostaLogin(Usuario usuario);
}
