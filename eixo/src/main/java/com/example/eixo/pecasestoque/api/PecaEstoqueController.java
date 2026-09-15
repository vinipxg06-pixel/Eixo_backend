package com.example.eixo.pecasestoque.api;

import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/pecas")
public class PecaEstoqueController {

    private final PecaEstoqueService pecaEstoqueService;

    public PecaEstoqueController(PecaEstoqueService pecaEstoqueService) {
        this.pecaEstoqueService = pecaEstoqueService;
    }

    @GetMapping
    public ResponseEntity<List<PecaEstoqueResponse>> getAllPecas(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(pecaEstoqueService.findAllByOficina(oficinaId));
    }

    @GetMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> getPecaById(@PathVariable Long oficinaId, @PathVariable Long estoqueId){
        return ResponseEntity.ok().body(pecaEstoqueService.findByIdAndOficinaId(estoqueId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<PecaEstoqueResponse> postPeca(@RequestBody PecaEstoqueRequest pecaEstoqueRequest){
        return ResponseEntity.ok().body(pecaEstoqueService.savePecaEstoque(pecaEstoqueRequest));
    }

    @PutMapping("/{estoqueId}")
    public ResponseEntity<PecaEstoqueResponse> putPeca(@RequestBody PecaEstoqueRequest pecaEstoqueRequest, @PathVariable Long estoqueId){
        return ResponseEntity.ok().body(pecaEstoqueService.updatePecaEstoque(pecaEstoqueRequest, estoqueId));
    }

    @DeleteMapping("/{estoqueId}")
    public ResponseEntity<Void> deletePeca(@PathVariable Long estoqueId, @PathVariable Long oficinaId){
        pecaEstoqueService.deletarPecaEstoque(estoqueId, oficinaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{estoqueId}/remover")
    public ResponseEntity<PecaEstoqueResponse> removerEstoque(@PathVariable Long estoqueId, @Valid @RequestBody PecaEstoqueRemoverRequest request) {
        return ResponseEntity.ok(pecaEstoqueService.removerPecaEstoque(estoqueId, request.quantidade()));
    }


    @PatchMapping("/{estoqueId}/adicionar")
    public ResponseEntity<PecaEstoqueResponse> adicionarEstoque(@PathVariable Long estoqueId, @RequestBody PecaEstoqueAdicionarRequest pecaEstoqueAdicionarRequest) {
        return ResponseEntity.ok(pecaEstoqueService.adicionarEstoque(estoqueId, pecaEstoqueAdicionarRequest));
    }

}
