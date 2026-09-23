package com.example.eixo.ordemservico.api;

import com.example.eixo.ordemservico.api.dto.OrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.api.dto.OrdemServicoUpdateRequest;
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

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> listar(@PathVariable Long oficinaId) {
        return ResponseEntity.ok(ordemServicoService.listaOrdensServico(oficinaId));
    }

    @GetMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> buscar(@PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(ordemServicoService.encontrarOrdemServicoPeloId(ordemServicoId, oficinaId));
    }

    @PostMapping("/cliente/{clienteId}/veiculo/{veiculoId}")
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody OrdemServicoRequest request, @PathVariable Long oficinaId, @PathVariable Long clienteId, @PathVariable Long veiculoId) {
        return ResponseEntity.status(201).body(ordemServicoService.salvarOrdemServico(request, oficinaId, clienteId, veiculoId));
    }

    @PutMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoResponse> atualizar(@Valid @RequestBody OrdemServicoUpdateRequest request, @PathVariable Long oficinaId, @PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(ordemServicoService.atualizarOrdemServico(request, ordemServicoId, oficinaId));
    }
}
