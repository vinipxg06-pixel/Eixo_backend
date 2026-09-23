package com.example.eixo.orcamento.api;

import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.request.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
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

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(orcamentoService.listaOrcamentos(oficinaId));
    }

    @GetMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoService.encontrarOrcamentoPeloId(orcamentoId, oficinaId));
    }

    @PostMapping("/cliente/{clienteId}/veiculo/{veiculoId}")
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest request, @PathVariable Long oficinaId, @PathVariable Long clienteId, @PathVariable Long veiculoId) {
        return ResponseEntity.status(201).body(orcamentoService.salvarOrcamento(request, oficinaId, clienteId, veiculoId));
    }

    @PutMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> atualizar(@Valid @RequestBody OrcamentoUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        return ResponseEntity.ok(orcamentoService.atualizarOrcamento(request, orcamentoId, oficinaId));
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
    public ResponseEntity<Void> deletar(@PathVariable Long oficinaId, @PathVariable Long orcamentoId) {
        orcamentoService.deletarOrcamento(orcamentoId, oficinaId);
        return ResponseEntity.noContent().build();
    }
}