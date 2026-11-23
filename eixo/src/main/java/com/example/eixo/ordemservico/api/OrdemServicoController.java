package com.example.eixo.ordemservico.api;

import com.example.eixo.ordemservico.api.dto.CancelarOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.FecharOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.api.dto.OrdemServicoUpdateRequest;
import com.example.eixo.ordemservico.service.OrdemServicoComPecasService;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;
    private final OrdemServicoComPecasService ordemServicoComPecasService;

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(ordemServicoService.listar(oficinaId));
    }

    @GetMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(ordemServicoService.buscar(ordemServicoId, oficinaId));
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody OrdemServicoRequest request, @PathVariable Long oficinaId) {
        return ResponseEntity.status(201).body(ordemServicoComPecasService.criar(request, oficinaId));
    }

    @PutMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> atualizar(@Valid @RequestBody OrdemServicoUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(ordemServicoComPecasService.atualizar(request, ordemServicoId, oficinaId));
    }

    /** Body opcional: sem body = todas as peças usadas por inteiro e sem confirmação de OS sem custo. */
    @PostMapping("/{ordemServicoId}/fechar")
    public ResponseEntity<OrdemServicoResponse> fechar(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId,
                                                       @Valid @RequestBody(required = false) FecharOrdemServicoRequest request) {
        FecharOrdemServicoRequest confirmacao = request == null ? new FecharOrdemServicoRequest(null, false) : request;
        return ResponseEntity.ok(ordemServicoComPecasService.fechar(ordemServicoId, oficinaId, confirmacao));
    }

    @PostMapping("/{ordemServicoId}/cancelar")
    public ResponseEntity<OrdemServicoResponse> cancelar(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId,
                                                         @Valid @RequestBody CancelarOrdemServicoRequest request) {
        return ResponseEntity.ok(ordemServicoComPecasService.cancelar(ordemServicoId, oficinaId, request));
    }
}
