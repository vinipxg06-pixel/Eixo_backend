package com.example.eixo.usuarios.api;

import com.example.eixo.usuarios.api.dto.*;
import com.example.eixo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@AllArgsConstructor
public class UsuariosController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse>getUsuarioById(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.findById(id));
    }

    @GetMapping("/oficinaUsuarios/{id}")
    public ResponseEntity<List<UsuarioResponse>> getUsuariosByOficinaID(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.findAllUsuariosByOficinaId(id));
    }

    @PostMapping("/oficinaUsuarios/{oficinaId}")
    public ResponseEntity<UsuarioResponse>postUsuario(@Valid @RequestBody UsuarioRequest usuariosRequest, @PathVariable Long oficinaId){
        return ResponseEntity.status(201).body(usuarioService.saveUsuario(usuariosRequest, oficinaId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>postLogin(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(201).body(usuarioService.login(loginRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse>putUsuario(@Valid @RequestBody UsuarioUpdateRequest usuarioUpdateRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.updateUsuario(usuarioUpdateRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id){
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
