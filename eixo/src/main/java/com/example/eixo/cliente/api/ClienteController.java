package com.example.eixo.cliente.api;


import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.api.dto.ClienteUpdateRequest;
import com.example.eixo.cliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.findAllClientes(oficinaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Long id, @PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.findClienteById(id, oficinaId));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> postCliente(@Valid @RequestBody ClienteRequest clienteRequest, @PathVariable Long oficinaId){
        return ResponseEntity.status(201).body(clienteService.saveCliente(clienteRequest, oficinaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> putCliente(@Valid @RequestBody ClienteUpdateRequest clienteUpdateRequest, @PathVariable Long id, @PathVariable Long oficinaId){
        return ResponseEntity.ok().body(clienteService.updateCliente(clienteUpdateRequest, id, oficinaId));
    }

}
