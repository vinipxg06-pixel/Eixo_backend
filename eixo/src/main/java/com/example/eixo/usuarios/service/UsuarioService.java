package com.example.eixo.usuarios.service;

import com.example.eixo.usuarios.api.dto.LoginRequest;
import com.example.eixo.usuarios.api.dto.UsuarioRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.api.dto.UsuarioUpdateRequest;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.Status;
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
        if(!usuario.getStatus().equals(Status.ATIVO)){
            throw new RuntimeException("usuario inativo");
        }
        UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);

        return usuarioResponse;
    }

    public UsuarioResponse updateUsuario(UsuarioUpdateRequest usuarioUpdateRequest, Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setEmail(usuarioUpdateRequest.email());
        usuario.setNomeUsuario(usuarioUpdateRequest.senha());
        usuario.setStatus(usuarioUpdateRequest.status());
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);

        return usuarioResponse;
    }

    public UsuarioResponse findById(Long id){
        Usuario usuarioEncontrado = usuarioRepository.findById(id).get();
        return usuarioMapper.toResponse(usuarioEncontrado);
    }

    public UsuarioResponse saveUsuario(UsuarioRequest usuarioRequest){
        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        usuario.setStatus(Status.ATIVO);
        Usuario usuarioSave = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSave);
    }

    public void deleteUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuarioRepository.delete(usuario);
    }
}
