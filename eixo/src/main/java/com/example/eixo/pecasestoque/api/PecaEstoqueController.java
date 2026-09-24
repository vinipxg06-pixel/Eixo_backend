package com.example.eixo.pecasestoque.api;

import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/pecas")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class PecaEstoqueController {

    private final PecaEstoqueService pecaEstoqueService;

    public PecaEstoqueController(PecaEstoqueService pecaEstoqueService) {
        this.pecaEstoqueService = pecaEstoqueService;
    }

    @GetMapping
    public ResponseEntity<List<PecaEstoqueResponse>> getAllPecas(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.findAllByOficina(oficinaId));
    }

    @GetMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> getPecaById(@PathVariable Long oficinaId, @PathVariable Long estoqueId) {
        return ResponseEntity.ok(pecaEstoqueService.findByIdAndOficinaId(estoqueId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<PecaEstoqueResponse> postPeca(@Valid @RequestBody PecaEstoqueRequest pecaEstoqueRequest, @PathVariable Long oficinaId) {
        return ResponseEntity.status(201).body(pecaEstoqueService.savePecaEstoque(pecaEstoqueRequest, oficinaId));
    }

    @PutMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> putPeca(@Valid @RequestBody PecaEstoqueRequest pecaEstoqueRequest, @PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.updatePecaEstoque(pecaEstoqueRequest, estoqueId, oficinaId));
    }

    @DeleteMapping("/{estoqueId}")
    public ResponseEntity<Void> deletePeca(@PathVariable Long estoqueId, @PathVariable Long oficinaId) {
        pecaEstoqueService.deletarPecaEstoque(estoqueId, oficinaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{estoqueId}/remover")
    public ResponseEntity<PecaEstoqueResponse> removerEstoque(@PathVariable Long estoqueId, @Valid @RequestBody PecaEstoqueRemoverRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.removerPecaEstoque(estoqueId, request.quantidade(), oficinaId));
    }

    @PatchMapping("/{estoqueId}/adicionar")
    public ResponseEntity<PecaEstoqueResponse> adicionarEstoque(@PathVariable Long estoqueId, @Valid @RequestBody PecaEstoqueAdicionarRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.ok(pecaEstoqueService.adicionarEstoque(estoqueId, request, oficinaId));
    }
}
