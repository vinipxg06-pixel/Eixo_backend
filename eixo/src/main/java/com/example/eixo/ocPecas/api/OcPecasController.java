package com.example.eixo.ocPecas.api;


import com.example.eixo.ocPecas.api.dto.request.OcPecasRequest;
import com.example.eixo.ocPecas.api.dto.response.OcPecasResponse;
import com.example.eixo.ocPecas.service.OcPecasService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocPecas/{orcamentoId}")
@AllArgsConstructor
public class OcPecasController {

        private final OcPecasService ocPecasService;

        @GetMapping
        public ResponseEntity<List<OcPecasResponse>> getAllOcPecasByOrcamento(@PathVariable Long orcamentoId){
            return ResponseEntity.ok().body(ocPecasService.getOcPecasByOrcamento(orcamentoId));
        }

        @PostMapping("/{estoqueId}")
        public ResponseEntity<OcPecasResponse> postOcPecas(@Valid @RequestBody OcPecasRequest ocPecasRequest, @PathVariable Long orcamentoId, @PathVariable Long estoqueId){
            return ResponseEntity.ok().body(ocPecasService.saveOcPeca(ocPecasRequest, orcamentoId, estoqueId));
        }

        @PutMapping("/{idOcPecas}")
        public ResponseEntity<OcPecasResponse> putOcPecas(@Valid @RequestBody OcPecasRequest ocPecasRequest, @PathVariable Long idOcPecas, @PathVariable Long orcamentoId){
            return ResponseEntity.ok().body(ocPecasService.putOcPecas(ocPecasRequest, idOcPecas, orcamentoId));
        }

        @DeleteMapping("/{idOcPecas}")
        public ResponseEntity<Void> deleteIdOcPecas(@PathVariable Long idOcPecas){
            ocPecasService.deleteOcPecas(idOcPecas);
            return ResponseEntity.noContent().build();
        }
}
