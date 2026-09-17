package com.example.eixo.orcamento.mapper;

import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.model.Orcamento;
import org.springframework.stereotype.Component;

@Component
public class OrcamentoMapper {

    public Orcamento transformarEmEntidade(OrcamentoRequest orcamentoRequest){
        Orcamento orcamento = new Orcamento();

        orcamento.setMaoDeObra(orcamentoRequest.maoDeObra());
        orcamento.setValorTotal(orcamentoRequest.valorTotal());
        orcamento.setDescricao(orcamentoRequest.descricao());
        return orcamento;
    }

    public OrcamentoResponse transformarEmResposta(Orcamento orcamento){
        return new OrcamentoResponse(
                orcamento.getIdOrcamento(),
                orcamento.getVeiculo().getIdVeiculo(),
                orcamento.getCliente().getClienteId(),
                orcamento.getStatus(),
                orcamento.getValorTotal(),
                orcamento.getDescricao()
        );

    }


}
