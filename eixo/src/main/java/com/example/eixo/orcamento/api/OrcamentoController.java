package com.example.eixo.orcamento.api;

import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.request.OrcamentoStatusRequest;
import com.example.eixo.orcamento.api.request.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas/{oficinaId}/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService){
        this.orcamentoService = orcamentoService;
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listaOrcamentos(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(orcamentoService.listaOrcamentos(oficinaId));
    }

    @GetMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> encontrarOrcamentoPeloId(@PathVariable Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.encontrarOrcamentoPeloId(orcamentoId));
    }

    @PostMapping("/{clienteId}/{idVeiculo}")
    public ResponseEntity<OrcamentoResponse> salvarOrcamento(@Valid @RequestBody OrcamentoRequest orcamentoRequest, @PathVariable Long oficinaId, @PathVariable Long clienteId, @PathVariable Long idVeiculo){
        return ResponseEntity.status(201).body(orcamentoService.salvarOrcamento(orcamentoRequest, oficinaId, clienteId, idVeiculo));
    }

    @PutMapping("/{orcamentoId}")
    public ResponseEntity<OrcamentoResponse> atualizarOrcamento(@Valid @RequestBody OrcamentoUpdateRequest orcamentoUpdateRequest, @PathVariable Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.atualizarOrcamento(orcamentoUpdateRequest, orcamentoId));
    }

    @PatchMapping("/{id}/alterarStatus")
    public ResponseEntity<OrcamentoResponse> alterarStatus(@Valid @RequestBody OrcamentoStatusRequest orcamentoStatusRequest, Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.alterarStatus(orcamentoId, orcamentoStatusRequest));
    }

    @DeleteMapping("/{orcamentoId}")
    public void deletarOrcamento(@PathVariable Long orcamentoId){
        orcamentoService.deletarOrcamento(orcamentoId);
    }

}
