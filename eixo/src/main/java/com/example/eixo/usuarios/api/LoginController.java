package com.example.eixo.usuarios.api;

import com.example.eixo.usuarios.api.dto.LoginRequest;
import com.example.eixo.usuarios.api.dto.LoginResponse;
import com.example.eixo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Login fica fora de /oficinas/{oficinaId}: é ele que descobre a oficina do usuário. */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(usuarioService.login(loginRequest));
    }
}
