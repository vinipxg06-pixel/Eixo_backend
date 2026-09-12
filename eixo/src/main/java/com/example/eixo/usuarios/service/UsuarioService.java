package com.example.eixo.usuarios.service;

import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import com.example.eixo.usuarios.api.dto.*;
import com.example.eixo.usuarios.exception.EmailJaCadastrado;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.UsuarioStatus;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final OficinaRepository oficinaRepository;

    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository, OficinaRepository oficinaRepository) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.oficinaRepository = oficinaRepository;
    }

    public List<UsuarioResponse> findAllUsuarios(){
        List<Usuario> listUsuario = usuarioRepository.findAll();
        List<UsuarioResponse> usuarioResponseList = new ArrayList<>();

        for(Usuario usuario : listUsuario){
            UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);
            usuarioResponseList.add(usuarioResponse);
        }

        return usuarioResponseList;
    }

    public List<UsuarioResponse> findAllUsuariosByOficinaId(Long oficinaId){
        List<Usuario> listUsuario = usuarioRepository.findAllByOficina_oficinaId(oficinaId);
        List<UsuarioResponse> usuarioResponseList = new ArrayList<>();

        for(Usuario usuario : listUsuario){
            UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);
            usuarioResponseList.add(usuarioResponse);
        }

        return usuarioResponseList;
    }

    public LoginResponse login(LoginRequest loginRequest){
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(!usuario.getSenha().equals(loginRequest.senha()) ) {
            throw new RuntimeException("Senha incorreta");
        }
        if(!usuario.getUsuarioStatus().equals(UsuarioStatus.ATIVO)){
            throw new RuntimeException("usuario inativo");
        }
        LoginResponse loginResponse = usuarioMapper.toLoginResponse(usuario);

        return loginResponse;
    }



    public UsuarioResponse updateUsuario(UsuarioUpdateRequest usuarioUpdateRequest, Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setEmail(usuarioUpdateRequest.email());
        usuario.setNomeUsuario(usuarioUpdateRequest.nomeUsuario());
        usuario.setSenha(usuarioUpdateRequest.senha());
        usuario.setUsuarioStatus(usuarioUpdateRequest.usuarioStatus());
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
        Oficina oficina = oficinaRepository.findById(usuarioRequest.oficinaId()).orElseThrow(() -> new RuntimeException("Oficina nao encontrada"));
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastrado("Email ja cadastrado");
        }
        usuario.setUsuarioStatus(UsuarioStatus.ATIVO);
        usuario.setOficina(oficina);
        Usuario usuarioSave = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSave);
    }

    public void deleteUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuarioRepository.delete(usuario);
    }


}
