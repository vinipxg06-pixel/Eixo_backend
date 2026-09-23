package com.example.eixo.orcamento.mapper;

import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.model.Orcamento;
import org.springframework.stereotype.Component;

@Component
public class OrcamentoMapper {

    public OrcamentoResponse transformarEmResposta(Orcamento orcamento) {
        return new OrcamentoResponse(
                orcamento.getIdOrcamento(),
                orcamento.getCliente().getClienteId(),
                orcamento.getVeiculo().getIdVeiculo(),
                orcamento.getStatus(),
                orcamento.getDescricao(),
                orcamento.getMaoDeObra(),
                orcamento.getValorTotal(),
                orcamento.getCreatedAt()
        );
    }

}