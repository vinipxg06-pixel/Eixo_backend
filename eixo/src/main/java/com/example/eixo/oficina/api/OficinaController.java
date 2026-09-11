package com.example.eixo.oficina.api;

import com.example.eixo.oficina.api.request.OficinaRequest;
import com.example.eixo.oficina.api.response.OficinaResponse;
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
      return ResponseEntity.ok().body(oficinaService.listarOficinas());
    };

    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponse> buscarOficina(@PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.encontrarOficinaPeloId(id));
    };

    @PostMapping
    public ResponseEntity<OficinaResponse> salvar(@Valid @RequestBody OficinaRequest oficinaRequest) {
        return ResponseEntity.ok().body(oficinaService.salvarOficina(oficinaRequest));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        oficinaService.deletarOficina(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponse> atualizarOficina(@Valid @RequestBody OficinaRequest oficinaRequest,@PathVariable Long id){
        return ResponseEntity.ok().body(oficinaService.atualizarOficina(oficinaRequest, id));
    }


}
