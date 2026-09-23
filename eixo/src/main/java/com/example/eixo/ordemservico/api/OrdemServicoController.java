package com.example.eixo.ordemservico.api;

import com.example.eixo.ordemservico.api.request.OrdemServicoResquest;
import com.example.eixo.ordemservico.api.request.OrdemServicoStatus;
import com.example.eixo.ordemservico.api.response.OrdemServicoResponse;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("oficinas/{oficinaId}/ordemservico")
@AllArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @GetMapping("/{ordemservicoId}")
    public ResponseEntity<OrdemServicoResponse> buscarOrdemServico(@PathVariable Long ordemServicoId){
        return ResponseEntity.ok().body(ordemServicoService.encontrarPeloId(ordemServicoId));
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> listarOrdensServico(@PathVariable Long oficinaId){
        return ResponseEntity.ok().body(ordemServicoService.listarOrdensServico(oficinaId));
    }

    @PutMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> atualizarOrdemServico(@Valid @RequestBody OrdemServicoResquest ordemServicoResquest, @PathVariable Long ordemServicoId){
        return ResponseEntity.ok().body(ordemServicoService.atualizarOrdemServico(ordemServicoResquest, ordemServicoId));
    }

    @PatchMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> atualizarStatusOrdemServico(@Valid @RequestBody OrdemServicoStatus ordemServicoStatus, @PathVariable Long ordemServicoId){
        return ResponseEntity.ok().body(ordemServicoService.atualizarStatusOrdemServico(ordemServicoStatus, ordemServicoId));
    }

    @DeleteMapping("/{ordemServicoId}")
    public void deletarOrdeServico(@PathVariable Long ordemServicoId){
        ordemServicoService.deletarOrdemServico(ordemServicoId);
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponse> salvarOrdemServico(@Valid @RequestBody OrdemServicoResquest ordemServicoResquest,@PathVariable Long oficinaId,@PathVariable Long clienteId,@PathVariable Long veiculoId,@PathVariable Long orcamentoId){
        return ResponseEntity.ok().body(ordemServicoService.criarOrdemServico(ordemServicoResquest, oficinaId, clienteId, veiculoId, orcamentoId));
    }
}
