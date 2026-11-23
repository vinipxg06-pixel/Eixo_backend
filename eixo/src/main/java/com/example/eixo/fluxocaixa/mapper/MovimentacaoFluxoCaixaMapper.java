package com.example.eixo.fluxocaixa.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaRequest;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaResponse;
import com.example.eixo.fluxocaixa.model.MovimentacaoFluxoCaixa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface MovimentacaoFluxoCaixaMapper {

    // id, data, vínculos, oficina e cancelamento são definidos pelo MovimentacaoFluxoCaixaService
    @Mapping(target = "idMovimentacao", ignore = true)
    @Mapping(target = "dataMovimentacao", ignore = true)
    @Mapping(target = "osVinculada", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "pecaEstoque", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    @Mapping(target = "cancelada", ignore = true)
    @Mapping(target = "canceladaEm", ignore = true)
    MovimentacaoFluxoCaixa paraEntidade(MovimentacaoFluxoCaixaRequest request);

    @Mapping(target = "ordemServicoId", source = "ordemServico.idOrdemServico")
    @Mapping(target = "estoqueId", source = "pecaEstoque.estoqueId")
    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    // regras da entidade, não os campos crus (cancelada pode estar null no banco)
    @Mapping(target = "automatica", expression = "java(movimentacao.isAutomatica())")
    @Mapping(target = "cancelada", expression = "java(movimentacao.estaCancelada())")
    MovimentacaoFluxoCaixaResponse paraResposta(MovimentacaoFluxoCaixa movimentacao);
}
