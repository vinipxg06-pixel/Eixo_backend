package com.example.eixo.Oficina.api;

import com.example.eixo.Oficina.api.request.OficinaRequest;
import com.example.eixo.Oficina.api.response.OficinaResponse;
import com.example.eixo.Oficina.model.Oficina;
import com.example.eixo.Oficina.service.OficinaService;
import jakarta.persistence.Entity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Spliterator;

@RestController
@RequestMapping("/oficinas")
@RequiredArgsConstructor
public class OficinaController {

    private final OficinaService oficinaService;

    @GetMapping
    public ResponseEntity<List<OficinaResponse>> listar(){
      return ResponseEntity.ok().body(oficinaService.listarOficinas());
    };

    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponse> buscarOficina(@PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.encontrarOficinaPeloId(id));
    };

    @PostMapping
    public ResponseEntity<OficinaResponse> salvar(@RequestBody OficinaRequest oficinaRequest) {
        return ResponseEntity.ok().body(oficinaService.salvarOficina(oficinaRequest));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        oficinaService.deletarOficina(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponse> atualizarOficina(@RequestBody OficinaRequest oficinaRequest,@PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.atualizarOficina(oficinaRequest, id));
    }


}
