package com.example.eixo.marcasmodelos.api;

import com.example.eixo.marcasmodelos.api.dto.ModeloResponse;
import com.example.eixo.marcasmodelos.service.ModeloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modelos")
@RequiredArgsConstructor
public class ModeloController {

    private final ModeloService modeloService;

    @GetMapping
    public ResponseEntity<List<ModeloResponse>> listar(){
        return ResponseEntity.ok().body(modeloService.listar());
    }

    @GetMapping("/marca/{idMarca}")
    public ResponseEntity<List<ModeloResponse>> listarPorMarca(@PathVariable Long idMarca){
        return ResponseEntity.ok().body(modeloService.listarPorMarca(idMarca));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloResponse> buscar(@PathVariable Long id){
        return ResponseEntity.ok().body(modeloService.buscar(id));
    }
}
