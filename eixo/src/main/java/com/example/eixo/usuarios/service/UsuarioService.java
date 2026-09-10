package com.example.eixo.usuarios.service;

import com.example.eixo.usuarios.api.dto.LoginRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponse> findAllUsuarios(){
        List<Usuario> listUsuario = usuarioRepository.findAll();
        List<UsuarioResponse> usuarioResponseList = new ArrayList<>();

        for(Usuario usuario : listUsuario){
            UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);
            usuarioResponseList.add(usuarioResponse);
        }

        return usuarioResponseList;
    }
    public UsuarioResponse login(LoginRequest loginRequest){
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(!usuario.getSenha().equals(loginRequest.senha()) ) {
            throw new RuntimeException("Senha incorreta");
        }

        UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);

        return usuarioResponse;
    }


}
