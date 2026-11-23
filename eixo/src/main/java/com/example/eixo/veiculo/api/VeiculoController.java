package com.example.eixo.veiculo.api;

import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.api.dto.VeiculoUpdateRequest;
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
    public ResponseEntity<List<VeiculoResponse>> listar(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(veiculoService.listar(oficinaId));
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> criar(@Valid @RequestBody VeiculoRequest veiculoRequest, @PathVariable Long oficinaId){
        return ResponseEntity.status(201).body(veiculoService.criar(veiculoRequest, oficinaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> atualizar(@Valid @RequestBody VeiculoUpdateRequest veiculoRequest, @PathVariable Long id, @PathVariable Long oficinaId){
        return ResponseEntity.ok().body(veiculoService.atualizar(veiculoRequest, id, oficinaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @PathVariable Long oficinaId){
        veiculoService.excluir(id, oficinaId);
        return ResponseEntity.noContent().build();
    }

}
