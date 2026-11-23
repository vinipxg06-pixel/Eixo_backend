package com.example.eixo.pecasestoque.api;

import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueUpdateRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/pecas")
@RequiredArgsConstructor
public class PecaEstoqueController {

    private final PecaEstoqueService pecaEstoqueService;

    @GetMapping
    public ResponseEntity<List<PecaEstoqueResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.listar(oficinaId));
    }

    @GetMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long estoqueId) {
        return ResponseEntity.ok(pecaEstoqueService.buscar(estoqueId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<PecaEstoqueResponse> criar(@Valid @RequestBody PecaEstoqueRequest pecaEstoqueRequest, @PathVariable Long oficinaId) {
        return ResponseEntity.status(201).body(pecaEstoqueService.criar(pecaEstoqueRequest, oficinaId));
    }

    @PutMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> atualizar(@Valid @RequestBody PecaEstoqueUpdateRequest pecaEstoqueUpdateRequest, @PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.atualizar(pecaEstoqueUpdateRequest, estoqueId, oficinaId));
    }

    @DeleteMapping("/{estoqueId}")
    public ResponseEntity<Void> excluir(@PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        pecaEstoqueService.excluir(estoqueId, oficinaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{estoqueId}/inativar")
    public ResponseEntity<PecaEstoqueResponse> inativar(@PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.inativar(estoqueId, oficinaId));
    }

    @PostMapping("/{estoqueId}/reativar")
    public ResponseEntity<PecaEstoqueResponse> reativar(@PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.reativar(estoqueId, oficinaId));
    }

    @PatchMapping("/{estoqueId}/remover")
    public ResponseEntity<PecaEstoqueResponse> retirarManualmente(@PathVariable Long estoqueId, @Valid @RequestBody PecaEstoqueRemoverRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.retirarManualmente(estoqueId, request, oficinaId));
    }

    @PatchMapping("/{estoqueId}/adicionar")
    public ResponseEntity<PecaEstoqueResponse> adicionarEstoque(@PathVariable Long estoqueId, @Valid @RequestBody PecaEstoqueAdicionarRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.adicionarEstoque(estoqueId, request, oficinaId));
    }
}
