package com.example.eixo.orcamento.api;

import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
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
    public ResponseEntity<List<OrcamentoResponse>> listaOrcamentos(Long oficinaId){
        return ResponseEntity.ok().body(orcamentoService.listaOrcamentos(oficinaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> encontrarOrcamentoPeloId(Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.encontrarOrcamentoPeloId(orcamentoId));
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> salvarOrcamento(OrcamentoRequest orcamentoRequest, Long oficinaId, Long clienteId, Long id){
        return ResponseEntity.status(201).body(orcamentoService.salvarOrcamento(orcamentoRequest, oficinaId, clienteId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizarOrcamento(OrcamentoRequest orcamentoRequest, Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.atualizarOrcamento(orcamentoRequest, orcamentoId));
    }

    @PatchMapping("/{id}/alterarStatus")
    public ResponseEntity<OrcamentoResponse> alterarStatus(StatusOrcamento status, Long orcamentoId){
        return ResponseEntity.ok().body(orcamentoService.alterarStatus(status, orcamentoId));
    }

    @DeleteMapping("/{id}")
    public void deletarOrcamento(Long orcamentoId){
        orcamentoService.deletarOrcamento(orcamentoId);
    }

}
