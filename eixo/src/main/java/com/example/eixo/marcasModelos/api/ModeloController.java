package com.example.eixo.marcasModelos.api;

import com.example.eixo.marcasModelos.api.dtos.ModeloResponse;
import com.example.eixo.marcasModelos.service.ModeloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @GetMapping
    public ResponseEntity<List<ModeloResponse>> getAllModelos(){
        return ResponseEntity.ok().body(modeloService.findAllModelos());
    }

    @GetMapping("/marca/{idMarca}")
    public ResponseEntity<List<ModeloResponse>> getAllModelosById_Marca(@PathVariable Long idMarca){
        return ResponseEntity.ok().body(modeloService.findAllModeloById_Marca(idMarca));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloResponse> getModeloById(@PathVariable Long id){
        return ResponseEntity.ok().body(modeloService.findModeloById(id));
    }
}
