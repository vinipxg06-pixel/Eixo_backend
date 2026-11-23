package com.example.eixo.veiculo.api;

import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Veículos vistos a partir do cliente: /oficinas/{oficinaId}/clientes/{clienteId}/veiculos. */
@RestController
@RequestMapping("/oficinas/{oficinaId}/clientes/{clienteId}/veiculos")
@RequiredArgsConstructor
public class VeiculosDoClienteController {

    private final VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> listar(@PathVariable Long oficinaId, @PathVariable Long clienteId) {
        return ResponseEntity.ok(veiculoService.listarPorCliente(clienteId, oficinaId));
    }
}
