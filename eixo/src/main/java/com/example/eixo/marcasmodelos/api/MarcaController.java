package com.example.eixo.marcasmodelos.api;

import com.example.eixo.marcasmodelos.api.dto.MarcaResponse;
import com.example.eixo.marcasmodelos.service.MarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listar(){
        return ResponseEntity.ok().body(marcaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> buscar(@PathVariable Long id){
        return ResponseEntity.ok().body(marcaService.buscar(id));
    }
}
