package com.example.eixo.cliente.api;


import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.api.dto.ClienteUpdateRequest;
import com.example.eixo.cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.listar(oficinaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id, @PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.buscar(id, oficinaId));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest, @PathVariable Long oficinaId){
        return ResponseEntity.status(201).body(clienteService.criar(clienteRequest, oficinaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@Valid @RequestBody ClienteUpdateRequest clienteUpdateRequest, @PathVariable Long id, @PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.atualizar(clienteUpdateRequest, id, oficinaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @PathVariable Long oficinaId){
        clienteService.excluir(id, oficinaId);
        return ResponseEntity.noContent().build();
    }

}
