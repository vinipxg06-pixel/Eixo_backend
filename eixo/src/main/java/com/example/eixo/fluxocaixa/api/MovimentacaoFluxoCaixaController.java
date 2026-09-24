package com.example.eixo.fluxocaixa.api;

import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaRequest;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaResponse;
import com.example.eixo.fluxocaixa.service.MovimentacaoFluxoCaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/fluxo-caixa")
@RequiredArgsConstructor
public class MovimentacaoFluxoCaixaController {

    private final MovimentacaoFluxoCaixaService movimentacaoFluxoCaixaService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoFluxoCaixaResponse>> listar(
            @PathVariable Long oficinaId
    ) {
        return ResponseEntity.ok(
                movimentacaoFluxoCaixaService.listarMovimentacoes(oficinaId)
        );
    }

    @GetMapping("/{idMovimentacao}")
    public ResponseEntity<MovimentacaoFluxoCaixaResponse> buscar(
            @PathVariable Long oficinaId,
            @PathVariable Long idMovimentacao
    ) {
        return ResponseEntity.ok(
                movimentacaoFluxoCaixaService.encontrarMovimentacaoPeloId(
                        idMovimentacao,
                        oficinaId
                )
        );
    }

    @PostMapping
    public ResponseEntity<MovimentacaoFluxoCaixaResponse> adicionar(
            @Valid @RequestBody MovimentacaoFluxoCaixaRequest request,
            @PathVariable Long oficinaId
    ) {
        return ResponseEntity.status(201).body(
                movimentacaoFluxoCaixaService.adicionarMovimentacao(
                        request,
                        oficinaId
                )
        );
    }

    @PutMapping("/{idMovimentacao}")
    public ResponseEntity<MovimentacaoFluxoCaixaResponse> atualizar(
            @Valid @RequestBody MovimentacaoFluxoCaixaRequest request,
            @PathVariable Long oficinaId,
            @PathVariable Long idMovimentacao
    ) {
        return ResponseEntity.ok(
                movimentacaoFluxoCaixaService.atualizarMovimentacao(
                        request,
                        idMovimentacao,
                        oficinaId
                )
        );
    }

    @DeleteMapping("/{idMovimentacao}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long oficinaId,
            @PathVariable Long idMovimentacao
    ) {
        movimentacaoFluxoCaixaService.deletarMovimentacao(
                idMovimentacao,
                oficinaId
        );

        return ResponseEntity.noContent().build();
    }
}
