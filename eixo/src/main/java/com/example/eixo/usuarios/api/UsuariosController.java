package com.example.eixo.usuarios.api
;

import com.example.eixo.usuarios.api.dto.LoginRequest;
import com.example.eixo.usuarios.api.dto.UsuarioRequest;
import com.example.eixo.usuarios.api.dto.UsuarioResponse;
import com.example.eixo.usuarios.api.dto.UsuarioUpdateRequest;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.usuarios.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuariosController {
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios(){
        return ResponseEntity.ok().body(usuarioService.findAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse>getUsuarioById(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse>postUsuario(@RequestBody UsuarioRequest usuariosRequest){
        return ResponseEntity.status(201).body(usuarioService.saveUsuario(usuariosRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse>postLogin(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(201).body(usuarioService.login(loginRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse>putUsuario(@RequestBody UsuarioUpdateRequest usuarioUpdateRequest,@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.updateUsuario(usuarioUpdateRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id){
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
