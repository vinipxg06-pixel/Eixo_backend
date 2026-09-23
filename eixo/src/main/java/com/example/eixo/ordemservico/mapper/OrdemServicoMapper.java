package com.example.eixo.ordemservico.mapper;

import com.example.eixo.ordemservico.api.dto.OrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.model.OrdemServico;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoMapper {

    public OrdemServico toEntity(OrdemServicoRequest ordemServicoRequest) {

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setDescricao(ordemServicoRequest.descricao());
        ordemServico.setMaoDeObra(ordemServicoRequest.maoDeObra());

        return ordemServico;
    }

    public OrdemServicoResponse transformarEmResposta(OrdemServico ordemServico) {

        return new OrdemServicoResponse(
                ordemServico.getIdOrdemServico(),
                ordemServico.getStatus(),
                ordemServico.getDescricao(),
                ordemServico.getValorTotal(),
                ordemServico.getDataAbertura(),
                ordemServico.getDataFechamento(),
                ordemServico.getMaoDeObra(),
                ordemServico.getOficina().getOficinaId(),
                ordemServico.getCliente().getClienteId(),
                ordemServico.getVeiculo().getIdVeiculo(),
                ordemServico.getOrcamento() != null
                        ? ordemServico.getOrcamento().getIdOrcamento()
                        : null,
                ordemServico.getCreatedAt(),
                ordemServico.getUpdatedAt()
        );
    }
}