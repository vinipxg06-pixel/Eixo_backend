package com.example.eixo.orcamento.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.ocpecas.service.OcPecasService;
import com.example.eixo.orcamento.api.dto.OrcamentoRequest;
import com.example.eixo.orcamento.api.dto.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.dto.OrcamentoResponse;
import com.example.eixo.orcamento.mapper.OrcamentoMapper;
import com.example.eixo.orcamento.model.Orcamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Salva o orçamento e suas peças numa única transação: se qualquer etapa falhar
 * (validação, estoque insuficiente, peça inativa...), nada é gravado.
 * A lista de peças do request é o estado final desejado (nula = peças não são alteradas).
 */
@Service
@RequiredArgsConstructor
public class OrcamentoComPecasService {

    private final OrcamentoService orcamentoService;
    private final OcPecasService ocPecasService;
    private final OrcamentoMapper orcamentoMapper;

    @Transactional
    public OrcamentoResponse criar(OrcamentoRequest request, Long oficinaId) {
        Orcamento orcamento = orcamentoService.criar(request, oficinaId);
        sincronizarPecas(orcamento, request.pecas(), oficinaId);
        return orcamentoMapper.paraResposta(orcamento);
    }

    @Transactional
    public OrcamentoResponse atualizar(OrcamentoUpdateRequest request, Long orcamentoId, Long oficinaId) {
        Orcamento orcamento = orcamentoService.atualizarDados(request, orcamentoId, oficinaId);
        sincronizarPecas(orcamento, request.pecas(), oficinaId);
        return orcamentoMapper.paraResposta(orcamento);
    }

    private void sincronizarPecas(Orcamento orcamento, List<LinhaPecaRequest> pecas, Long oficinaId) {
        if (pecas != null) {
            ocPecasService.sincronizar(orcamento, pecas, oficinaId);
        }
        orcamentoService.recalcularTotal(orcamento);
    }
}
