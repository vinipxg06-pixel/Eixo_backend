package com.example.eixo.ocpecas.api;

import com.example.eixo.ocpecas.api.dto.OcPecasRequest;
import com.example.eixo.ocpecas.api.dto.OcPecasUpdateRequest;
import com.example.eixo.ocpecas.api.dto.OcPecasResponse;
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
        return ResponseEntity.ok(ocPecasService.listar(oficinaId, orcamentoId));
    }

    @PostMapping
    public ResponseEntity<OcPecasResponse> criar(@Valid @RequestBody OcPecasRequest request, @PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.status(201).body(ocPecasService.criar(oficinaId, orcamentoId, request));
    }

    @PatchMapping("/{idOcPeca}/quantidade")
    public ResponseEntity<OcPecasResponse> atualizar(@Valid @RequestBody OcPecasUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long orcamentoId, @PathVariable Long idOcPeca) {
        return ResponseEntity.ok(ocPecasService.atualizar(oficinaId, orcamentoId, idOcPeca, request));
    }

    @DeleteMapping("/{idOcPeca}")
    public ResponseEntity<Void> excluir(@PathVariable Long oficinaId, @PathVariable Long orcamentoId, @PathVariable Long idOcPeca) {
        ocPecasService.excluir(oficinaId, orcamentoId, idOcPeca);
        return ResponseEntity.noContent().build();
    }
}