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
    public class VeiculoController {

        private final VeiculoService veiculoService;

        @GetMapping
        public ResponseEntity<List<VeiculoResponse>> getAllVeiculos(@PathVariable Long oficinaId){
            return ResponseEntity.ok().body(veiculoService.listarVeiculosPorOficina(oficinaId));
        }

        @PostMapping("/{clienteId}/{idModelo}")
        public ResponseEntity<VeiculoResponse> postVeiculo(@Valid @RequestBody VeiculoRequest veiculoRequest, @PathVariable Long oficinaId, @PathVariable Long clienteId, @PathVariable Long idModelo){
            return ResponseEntity.ok().body(veiculoService.cadastrar(veiculoRequest, oficinaId, clienteId, idModelo));
        }

        @GetMapping("/{clienteId}")
        public ResponseEntity<List<VeiculoResponse>> getVeiculosByClienteId(@PathVariable Long clienteId){
            return ResponseEntity.ok().body(veiculoService.listarVeiculosPorCliente(clienteId));
        }

        @PutMapping("/{id}/{idModelo}")
        public ResponseEntity<VeiculoResponse> putVeiculo (@Valid @RequestBody VeiculoRequest veiculoRequest, @PathVariable Long id, @PathVariable Long idModelo){
            return ResponseEntity.ok().body(veiculoService.atualizarVeiculo(veiculoRequest, id, idModelo));
        }

        @DeleteMapping("/{id}")
        public void deleteVeiculo(@PathVariable Long id){
            veiculoService.deletar(id);
        }

    }