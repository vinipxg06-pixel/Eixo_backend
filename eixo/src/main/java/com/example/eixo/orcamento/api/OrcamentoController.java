package com.example.eixo.orcamento.api;

import com.example.eixo.orcamento.api.dto.OrcamentoRequest;
import com.example.eixo.orcamento.api.dto.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.dto.OrcamentoResponse;
import com.example.eixo.orcamento.service.OrcamentoComPecasService;
import com.example.eixo.orcamento.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;
    private final OrcamentoComPecasService orcamentoComPecasService;

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(orcamentoService.listar(oficinaId));
    }

    @GetMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoService.buscar(orcamentoId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.status(201).body(orcamentoComPecasService.criar(request, oficinaId));
    }

    @PutMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> atualizar(@Valid @RequestBody OrcamentoUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoComPecasService.atualizar(request, orcamentoId, oficinaId));
    }

    @PostMapping("/{orcamentoId}/aprovar")
    public ResponseEntity<OrcamentoResponse> aprovar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoService.aprovar(orcamentoId, oficinaId));
    }

    @PostMapping("/{orcamentoId}/recusar")
    public ResponseEntity<OrcamentoResponse> recusar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoService.recusar(orcamentoId, oficinaId));
    }

    @DeleteMapping("/{orcamentoId}")
    public ResponseEntity<Void> excluir(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        orcamentoService.excluir(orcamentoId, oficinaId);
        return ResponseEntity.noContent().build();
    }
}