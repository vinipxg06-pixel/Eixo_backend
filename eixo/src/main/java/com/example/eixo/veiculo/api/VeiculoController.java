package com.example.eixo.veiculo.api;

import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/oficinas/{oficinaId}/veiculos")
    @RequiredArgsConstructor
    @CrossOrigin(origins = {
            "http://localhost:5500",
            "http://127.0.0.1:5500"
    })
    public class VeiculoController {

        private final VeiculoService veiculoService;

        @GetMapping
        public ResponseEntity<List<VeiculoResponse>> getAllVeiculos(@PathVariable Long oficinaId){
            return ResponseEntity.ok().body(veiculoService.listarVeiculosPorOficina(oficinaId));
        }

        @PostMapping("/{clienteId}/{idModelo}")
        public ResponseEntity<VeiculoResponse> postVeiculo(@Valid @RequestBody VeiculoRequest veiculoRequest, @PathVariable Long oficinaId, @PathVariable Long clienteId, @PathVariable Long idModelo){
            return ResponseEntity.status(201).body(veiculoService.cadastrar(veiculoRequest, oficinaId, clienteId, idModelo));
        }

        @GetMapping("/{clienteId}")
        public ResponseEntity<List<VeiculoResponse>> getVeiculosByClienteId(@PathVariable Long clienteId, @PathVariable Long oficinaId){
            return ResponseEntity.ok().body(veiculoService.listarVeiculosPorCliente(clienteId, oficinaId));
        }

        @PutMapping("/{id}/{idModelo}")
        public ResponseEntity<VeiculoResponse> putVeiculo (@Valid @RequestBody VeiculoRequest veiculoRequest, @PathVariable Long id, @PathVariable Long idModelo, @PathVariable Long oficinaId){
            return ResponseEntity.ok().body(veiculoService.atualizarVeiculo(veiculoRequest, id, idModelo, oficinaId));
        }

        @DeleteMapping("/{id}")
        public void deleteVeiculo(@PathVariable Long id, @PathVariable Long oficinaId){
            veiculoService.deletar(id, oficinaId);
        }

    }