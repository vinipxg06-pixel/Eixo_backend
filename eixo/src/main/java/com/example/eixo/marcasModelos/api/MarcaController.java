package com.example.eixo.marcasModelos.api;

import com.example.eixo.marcasModelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasModelos.service.MarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<MarcaResponse>> getAllMarcas(){
        return ResponseEntity.ok().body(marcaService.findAllMarcas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> getMarcaById(@PathVariable Long id){
        return ResponseEntity.ok().body(marcaService.findMarcaById(id));
    }
}
