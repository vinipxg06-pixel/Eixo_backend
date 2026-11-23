package com.example.eixo.usuarios.api;

import com.example.eixo.usuarios.api.dto.UsuarioRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.api.dto.UsuarioUpdateRequest;
import com.example.eixo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/usuarios")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(usuarioService.listar(oficinaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscar(id, oficinaId));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest usuarioRequest, @PathVariable Long oficinaId) {
        return ResponseEntity.status(201).body(usuarioService.criar(usuarioRequest, oficinaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@Valid @RequestBody UsuarioUpdateRequest usuarioUpdateRequest, @PathVariable Long oficinaId, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.atualizar(usuarioUpdateRequest, id, oficinaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long oficinaId, @PathVariable Long id) {
        usuarioService.excluir(id, oficinaId);
        return ResponseEntity.noContent().build();
    }
}
