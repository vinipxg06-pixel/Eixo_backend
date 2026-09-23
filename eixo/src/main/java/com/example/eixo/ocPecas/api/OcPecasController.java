// ocpecas/api/OcPecasController.java
package com.example.eixo.ocpecas.api;

import com.example.eixo.ocpecas.api.dto.request.OcPecasRequest;
import com.example.eixo.ocpecas.api.dto.response.OcPecasResponse;
import com.example.eixo.ocpecas.service.OcPecasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/orcamentos/{orcamentoId}/pecas")
@RequiredArgsConstructor
public class OcPecasController {

    private final OcPecasService ocPecasService;

    @GetMapping
    public ResponseEntity<List<OcPecasResponse>> listar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(ocPecasService.listarPorOrcamento(oficinaId, orcamentoId));
    }

    @PostMapping("/{estoqueId}")
    public ResponseEntity<OcPecasResponse> adicionar(@Valid @RequestBody OcPecasRequest request,
                                                     @PathVariable Long oficinaId,
                                                     @PathVariable Long orcamentoId,
                                                     @PathVariable Long estoqueId) {
        return ResponseEntity.status(201)
                .body(ocPecasService.adicionarPeca(oficinaId, orcamentoId, estoqueId, request));
    }

    @PatchMapping("/{idOcPeca}/quantidade")
    public ResponseEntity<OcPecasResponse> alterarQuantidade(
            @Valid @RequestBody OcPecasRequest request,
            @PathVariable Long oficinaId,
            @PathVariable Long orcamentoId,
            @PathVariable Long idOcPeca) {
        return ResponseEntity.ok(
                ocPecasService.alterarQuantidade(oficinaId, orcamentoId, idOcPeca, request));
    }

    @DeleteMapping("/{idOcPeca}")
    public ResponseEntity<Void> remover(@PathVariable Long oficinaId,
                                        @PathVariable Long orcamentoId,
                                        @PathVariable Long idOcPeca) {
        ocPecasService.removerPeca(oficinaId, orcamentoId, idOcPeca);
        return ResponseEntity.noContent().build();
    }
}