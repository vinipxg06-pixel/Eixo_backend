package com.example.eixo.fluxoCaixa.api;

import com.example.eixo.fluxoCaixa.api.dto.CaixaRequest;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;
import com.example.eixo.fluxoCaixa.service.CaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("oficinas/caixa/")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService caixaService;

    @GetMapping("{caixaId}")
    public ResponseEntity<CaixaResponse> buscarPorId(@PathVariable Long caixaId) {
        CaixaResponse caixa = caixaService.encontrarfluxoCaixapeloId(caixaId);

        return ResponseEntity.ok().body(caixa);
    }


    @GetMapping("{oficinaId}")
    public ResponseEntity<List<CaixaResponse>> listarCaixa(@PathVariable Long oficinaId) {
        return ResponseEntity.ok().body(caixaService.listarCaixa(oficinaId));
    }

    @PutMapping("{caixaId}")
    public ResponseEntity<CaixaResponse> atualizarCaixa(@Valid @RequestBody CaixaRequest caixaRequest, @PathVariable Long caixaId) {
        return ResponseEntity.ok().body(caixaService.atualizarCaixa(caixaRequest, caixaId));
    }


    @DeleteMapping("{caixaId}")
    public void deletarCaixa(@PathVariable Long caixaId) {
        caixaService.deletarCaixa(caixaId);
    }

    @PostMapping
    public ResponseEntity<CaixaResponse> salvarCaixa(@Valid @RequestBody CaixaRequest caixaRequest) {
        return ResponseEntity.ok().body(caixaService.salvarCaixa(caixaRequest));
    }
}



