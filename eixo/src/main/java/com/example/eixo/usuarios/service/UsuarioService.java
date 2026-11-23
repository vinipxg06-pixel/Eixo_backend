package com.example.eixo.usuarios.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.CredenciaisInvalidas;
import com.example.eixo.excecao.excecoespersonalizadas.UsuarioInativo;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.usuarios.api.dto.*;
import com.example.eixo.excecao.excecoespersonalizadas.RegistroDuplicado;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.UsuarioStatus;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final OficinaService oficinaService;
    private final PasswordEncoder passwordEncoder;

    public Usuario buscarEntidade(Long id, Long oficinaId){
        return usuarioRepository.findByIdUsuarioAndOficina_OficinaId(id, oficinaId).orElseThrow(() -> new RecursoNaoEncontrado("Usuário com Id: " + id + " não encontrado"));
    }

    public List<UsuarioResponse> listar(Long oficinaId){
        List<Usuario> listUsuario = usuarioRepository.findAllByOficina_oficinaId(oficinaId);
        List<UsuarioResponse> usuarioResponseList = new ArrayList<>();

        for(Usuario usuario : listUsuario){
            UsuarioResponse usuarioResponse = usuarioMapper.paraResposta(usuario);
            usuarioResponseList.add(usuarioResponse);
        }

        return usuarioResponseList;
    }

    public LoginResponse login(LoginRequest loginRequest){
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RecursoNaoEncontrado("Usuário não encontrado"));
        if(!senhaConfere(loginRequest.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidas("Senha incorreta");
        }
        if(usuario.getUsuarioStatus() != UsuarioStatus.Ativo){
            throw new UsuarioInativo("Usuário inativo");
        }
        LoginResponse loginResponse = usuarioMapper.paraRespostaLogin(usuario);

        return loginResponse;
    }

    public UsuarioResponse atualizar(UsuarioUpdateRequest usuarioUpdateRequest, Long id, Long oficinaId){
        Usuario usuario = buscarEntidade(id, oficinaId);
        if (usuarioRepository.existsByEmailAndIdUsuarioNot(usuarioUpdateRequest.email(), id)) {
            throw new RegistroDuplicado("Email ja cadastrado");
        }

        usuario.setEmail(usuarioUpdateRequest.email());
        usuario.setNomeUsuario(usuarioUpdateRequest.nomeUsuario());
        usuario.setSenha(passwordEncoder.encode(usuarioUpdateRequest.senha()));
        usuario.setUsuarioStatus(usuarioUpdateRequest.usuarioStatus());

        return usuarioMapper.paraResposta(usuarioRepository.save(usuario));
    }

    public UsuarioResponse buscar(Long id, Long oficinaId){
        Usuario usuarioEncontrado = buscarEntidade(id, oficinaId);
        return usuarioMapper.paraResposta(usuarioEncontrado);
    }

    public UsuarioResponse criar(UsuarioRequest usuarioRequest, Long oficinaId){
        Usuario usuario = usuarioMapper.paraEntidade(usuarioRequest);
        Oficina oficina = oficinaService.buscarEntidade(oficinaId);
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RegistroDuplicado("Email ja cadastrado");
        }
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        usuario.setUsuarioStatus(UsuarioStatus.Ativo);
        usuario.setOficina(oficina);
        Usuario usuarioSave = usuarioRepository.save(usuario);
        return usuarioMapper.paraResposta(usuarioSave);
    }

    public void excluir(Long id, Long oficinaId){
        Usuario usuario = buscarEntidade(id, oficinaId);
        usuarioRepository.delete(usuario);
    }

    private boolean senhaConfere(String senhaInformada, String hashArmazenado) {
        return hashArmazenado != null && passwordEncoder.matches(senhaInformada, hashArmazenado);
    }
}
