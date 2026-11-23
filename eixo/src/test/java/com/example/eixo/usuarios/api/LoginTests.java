package com.example.eixo.usuarios.api;

import com.example.eixo.excecao.ManipuladorExcecoes;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.usuarios.api.dto.LoginResponse;
import com.example.eixo.usuarios.mapper.UsuarioMapper;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.model.UsuarioStatus;
import com.example.eixo.usuarios.repository.UsuarioRepository;
import com.example.eixo.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginTests {
    private UsuarioRepository repository;
    private UsuarioMapper mapper;
    private MockMvc mvc;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void configurar() {
        repository = mock(UsuarioRepository.class);
        mapper = mock(UsuarioMapper.class);
        UsuarioService service = new UsuarioService(mapper, repository, mock(OficinaService.class), passwordEncoder);
        mvc = MockMvcBuilders.standaloneSetup(new LoginController(service))
                .setControllerAdvice(new ManipuladorExcecoes())
                .build();
    }

    @Test
    void usuarioNaoEncontradoRetorna404ComMensagem() throws Exception {
        when(repository.findByEmail("teste@example.com")).thenReturn(Optional.empty());
        verificarErro(404, "Usuário não encontrado");
    }

    @Test
    void senhaIncorretaRetorna401ComMensagem() throws Exception {
        usuario("outra-senha", UsuarioStatus.Ativo);
        verificarErro(401, "Senha incorreta");
    }

    @Test
    void usuarioInativoRetorna403ComMensagem() throws Exception {
        usuario("senha-teste", UsuarioStatus.Inativo);
        verificarErro(403, "Usuário inativo");
    }

    @Test
    void senhaNulaRetorna401() throws Exception {
        usuario(null, UsuarioStatus.Ativo);
        verificarErro(401, "Senha incorreta");
    }

    @Test
    void statusNuloRetorna403() throws Exception {
        usuario("senha-teste", null);
        verificarErro(403, "Usuário inativo");
    }

    @Test
    void loginValidoRetorna200ComOficina() throws Exception {
        Usuario usuario = usuario("senha-teste", UsuarioStatus.Ativo);
        when(mapper.paraRespostaLogin(usuario)).thenReturn(new LoginResponse(10L));
        login().andExpect(status().isOk()).andExpect(jsonPath("$.oficinaId").value(10));
    }

    @Test
    void camposVaziosRetornam400ComListaDeCampos() throws Exception {
        mvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"","senha":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[?(@.field == 'email')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'senha')]").exists());
        verifyNoInteractions(repository);
    }

    private Usuario usuario(String senha, UsuarioStatus status) {
        Usuario usuario = new Usuario();
        usuario.setSenha(senha == null ? null : passwordEncoder.encode(senha));
        usuario.setUsuarioStatus(status);
        when(repository.findByEmail("teste@example.com")).thenReturn(Optional.of(usuario));
        return usuario;
    }

    private ResultActions login() throws Exception {
        return mvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"teste@example.com","senha":"senha-teste"}
                        """));
    }

    private void verificarErro(int codigo, String mensagem) throws Exception {
        login().andExpect(status().is(codigo))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(codigo))
                .andExpect(jsonPath("$.message").value(mensagem))
                .andExpect(jsonPath("$.error").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/usuarios/login"));
        verifyNoInteractions(mapper);
    }
}
