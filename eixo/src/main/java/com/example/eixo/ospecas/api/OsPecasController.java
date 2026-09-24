package com.example.eixo.ospecas.api;

import com.example.eixo.ospecas.api.dto.OsPecasRequest;
import com.example.eixo.ospecas.api.dto.OsPecasResponse;
import com.example.eixo.ospecas.api.dto.OsPecasUpdateRequest;
import com.example.eixo.ospecas.service.OsPecasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oficinas/{oficinaId}/ordens-servico/{ordemServicoId}/pecas")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class OsPecasController {

    private final OsPecasService osPecasService;

    @PostMapping
    public ResponseEntity<OsPecasResponse> adicionar(@Valid @RequestBody OsPecasRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.status(201).body(osPecasService.adicionarPeca(ordemServicoId, oficinaId, request));
    }

    @PatchMapping("/{osPecasId}/quantidade")
    public ResponseEntity<OsPecasResponse> alterarQuantidade(@Valid @RequestBody OsPecasUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId, @PathVariable Long osPecasId) {
        return ResponseEntity.ok(osPecasService.atualizarQuantidade(osPecasId, ordemServicoId, oficinaId, request));
    }

    @DeleteMapping("/{osPecasId}")
    public ResponseEntity<Void> remover(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId, @PathVariable Long osPecasId) {
        osPecasService.removerPeca(osPecasId, ordemServicoId, oficinaId);
        return ResponseEntity.noContent().build();
    }
}
