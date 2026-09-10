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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(){
        return ResponseEntity.ok().body(clienteService.findAllClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Long id){
        return ResponseEntity.ok().body(clienteService.findClienteById(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> postCliente(@Valid @RequestBody ClienteRequest clienteRequest){
        return ResponseEntity.status(201).body(clienteService.saveCliente(clienteRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> putCliente(@Valid @RequestBody ClienteUpdateRequest clienteUpdateRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(clienteService.updateCliente(clienteUpdateRequest, id));
    }

}
