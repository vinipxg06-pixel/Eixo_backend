package com.example.eixo.usuarios.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.usuarios.api.dto.*;
import com.example.eixo.usuarios.exception.excecoesPersonalizadas.EmailJaCadastrado;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.UsuarioStatus;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final OficinaService oficinaService;

    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository, OficinaService oficinaService) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.oficinaService = oficinaService;
    }

    public Usuario encontrePeloId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontrado("Usuário com Id: " + id + " não encontrado"));
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
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RecursoNaoEncontrado("Usuário não encontrado"));
        if(!usuario.getSenha().equals(loginRequest.senha()) ) {
            throw new RuntimeException("Senha incorreta");
        }
        if(!usuario.getUsuarioStatus().equals(UsuarioStatus.Ativo)){
            throw new RuntimeException("usuario inativo");
        }
        LoginResponse loginResponse = usuarioMapper.toLoginResponse(usuario);

        return loginResponse;
    }

    public UsuarioResponse updateUsuario(UsuarioUpdateRequest usuarioUpdateRequest, Long id){

        Usuario usuario = encontrePeloId(id);

        usuario.setEmail(usuarioUpdateRequest.email());
        usuario.setNomeUsuario(usuarioUpdateRequest.nomeUsuario());
        usuario.setSenha(usuarioUpdateRequest.senha());
        usuario.setUsuarioStatus(usuarioUpdateRequest.usuarioStatus());
        usuario.setUpdatedAt(LocalDateTime.now());
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        UsuarioResponse usuarioResponse = usuarioMapper.toResponse(usuario);

        return usuarioResponse;
    }

    public UsuarioResponse findById(Long id){
        Usuario usuarioEncontrado = usuarioRepository.findById(id).get();
        return usuarioMapper.toResponse(usuarioEncontrado);
    }

    public UsuarioResponse saveUsuario(UsuarioRequest usuarioRequest, Long oficinaId){
        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        Oficina oficina = oficinaService.findById(oficinaId);
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastrado("Email ja cadastrado");
        }
        usuario.setUsuarioStatus(UsuarioStatus.Ativo);
        usuario.setOficina(oficina);
        Usuario usuarioSave = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSave);
    }

    public void deleteUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuarioRepository.delete(usuario);
    }


}
