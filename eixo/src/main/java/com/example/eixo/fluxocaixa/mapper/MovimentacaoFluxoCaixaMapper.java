package com.example.eixo.fluxocaixa.mapper;

import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaRequest;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaResponse;
import com.example.eixo.fluxocaixa.model.MovimentacaoFluxoCaixa;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoFluxoCaixaMapper {

    public MovimentacaoFluxoCaixa transformarEmEntidade(MovimentacaoFluxoCaixaRequest request) {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setDescricao(request.descricao());
        movimentacao.setTipo(request.tipo());
        movimentacao.setValor(request.valor());
        movimentacao.setCategoria(request.categoria());
        return movimentacao;
    }

    public MovimentacaoFluxoCaixaResponse transformarEmResposta(MovimentacaoFluxoCaixa movimentacao) {
        return new MovimentacaoFluxoCaixaResponse(
                movimentacao.getIdMovimentacao(),
                movimentacao.getDescricao(),
                movimentacao.getTipo(),
                movimentacao.getValor(),
                movimentacao.getCategoria(),
                movimentacao.getDataMovimentacao(),
                movimentacao.getOsVinculada(),
                movimentacao.getOrdemServico() != null
                        ? movimentacao.getOrdemServico().getIdOrdemServico()
                        : null,
                movimentacao.getPecaEstoque() != null
                        ? movimentacao.getPecaEstoque().getEstoqueId()
                        : null,
                movimentacao.getOficina().getOficinaId()
        );
    }
}
