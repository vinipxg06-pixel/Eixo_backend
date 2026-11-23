package com.example.eixo.ordemservico.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.ordemservico.api.dto.CancelarOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.FecharOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.api.dto.OrdemServicoUpdateRequest;
import com.example.eixo.ordemservico.mapper.OrdemServicoMapper;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ospecas.service.OsPecasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Salva a ordem de serviço (inclusive a movimentação de estoque) e suas peças numa única transação: se qualquer etapa falhar
 * (validação, estoque insuficiente, peça inativa...), nada é gravado.
 * A lista de peças do request é o estado final desejado (nula = peças não são alteradas).
 */
@Service
@RequiredArgsConstructor
public class OrdemServicoComPecasService {

    private final OrdemServicoService ordemServicoService;
    private final OsPecasService osPecasService;
    private final OrdemServicoMapper ordemServicoMapper;

    @Transactional
    public OrdemServicoResponse criar(OrdemServicoRequest request, Long oficinaId) {
        OrdemServico ordemServico = ordemServicoService.criar(request, oficinaId);
        sincronizarPecas(ordemServico, request.pecas(), oficinaId);
        return ordemServicoMapper.paraResposta(ordemServico);
    }

    @Transactional
    public OrdemServicoResponse atualizar(OrdemServicoUpdateRequest request, Long ordemServicoId, Long oficinaId) {
        OrdemServico ordemServico = ordemServicoService.atualizarDados(request, ordemServicoId, oficinaId);
        sincronizarPecas(ordemServico, request.pecas(), oficinaId);
        return ordemServicoMapper.paraResposta(ordemServico);
    }

    /**
     * Fechamento confirmado pelo usuário: primeiro aplica o uso real das peças (o que não foi usado volta ao estoque
     * e deixa de ser cobrado), depois fecha a OS com o total recalculado e lança a receita.
     * Se o fechamento for recusado (ex.: OS sem valor sem confirmação), a devolução também é desfeita.
     */
    @Transactional
    public OrdemServicoResponse fechar(Long ordemServicoId, Long oficinaId, FecharOrdemServicoRequest request) {
        OrdemServico ordemServico = ordemServicoService.buscarEntidade(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();
        if (request.pecas() != null) {
            osPecasService.ajustarUsoNoFechamento(ordemServico, request.pecas(), oficinaId);
        }
        return ordemServicoService.fecharOrdemServico(ordemServicoId, oficinaId, request.confirmarSemCusto());
    }

    /** Cancela a OS e devolve todas as peças ao estoque. Nada é lançado no caixa. */
    @Transactional
    public OrdemServicoResponse cancelar(Long ordemServicoId, Long oficinaId, CancelarOrdemServicoRequest request) {
        OrdemServico ordemServico = ordemServicoService.cancelar(ordemServicoId, oficinaId, request.motivo());
        osPecasService.devolverTodasAoEstoque(ordemServico, oficinaId);
        return ordemServicoMapper.paraResposta(ordemServico);
    }

    private void sincronizarPecas(OrdemServico ordemServico, List<LinhaPecaRequest> pecas, Long oficinaId) {
        if (pecas != null) {
            osPecasService.sincronizar(ordemServico, pecas, oficinaId);
        }
        ordemServicoService.recalcularTotal(ordemServico);
    }
}
