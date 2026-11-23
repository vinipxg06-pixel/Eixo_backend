package com.example.eixo.oficina.api;

import com.example.eixo.oficina.api.dto.OficinaRequest;
import com.example.eixo.oficina.api.dto.OficinaResponse;
import com.example.eixo.oficina.service.OficinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas")
@RequiredArgsConstructor
public class OficinaController {

    private final OficinaService oficinaService;

    @GetMapping
    public ResponseEntity<List<OficinaResponse>> listar(){
      return ResponseEntity.ok().body(oficinaService.listar());
    };

    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponse> buscar(@PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.buscar(id));
    };

    @PostMapping
    public ResponseEntity<OficinaResponse> criar(@Valid @RequestBody OficinaRequest oficinaRequest) {
        return ResponseEntity.status(201).body(oficinaService.criar(oficinaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        oficinaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponse> atualizar(@Valid @RequestBody OficinaRequest oficinaRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.atualizar(oficinaRequest, id));
    }


}
