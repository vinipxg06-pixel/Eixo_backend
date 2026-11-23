package com.example.eixo.ospecas.api;

import com.example.eixo.ospecas.api.dto.OsPecasRequest;
import com.example.eixo.ospecas.api.dto.OsPecasResponse;
import com.example.eixo.ospecas.api.dto.OsPecasUpdateRequest;
import com.example.eixo.ospecas.service.OsPecasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/ordens-servico/{ordemServicoId}/pecas")
@RequiredArgsConstructor
public class OsPecasController {

    private final OsPecasService osPecasService;

    @GetMapping
    public ResponseEntity<List<OsPecasResponse>> listar(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId){
        return ResponseEntity.ok().body(osPecasService.listar(ordemServicoId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<OsPecasResponse> criar(@Valid @RequestBody OsPecasRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.status(201).body(osPecasService.criar(ordemServicoId, oficinaId, request));
    }

    @PatchMapping("/{osPecasId}/quantidade")
    public ResponseEntity<OsPecasResponse> atualizar(@Valid @RequestBody OsPecasUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId, @PathVariable Long osPecasId) {
        return ResponseEntity.ok(osPecasService.atualizar(osPecasId, ordemServicoId, oficinaId, request));
    }

    @DeleteMapping("/{osPecasId}")
    public ResponseEntity<Void> excluir(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId, @PathVariable Long osPecasId) {
        osPecasService.excluir(osPecasId, ordemServicoId, oficinaId);
        return ResponseEntity.noContent().build();
    }
}
