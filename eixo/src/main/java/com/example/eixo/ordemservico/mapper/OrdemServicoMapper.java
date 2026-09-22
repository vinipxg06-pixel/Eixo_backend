package com.example.eixo.ordemservico.mapper;

import com.example.eixo.ordemservico.api.request.OrdemServicoResquest;
import com.example.eixo.ordemservico.api.response.OrdemServicoResponse;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.model.StatusOS;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoMapper {

    public OrdemServico tranformarEmEntidade(OrdemServicoResquest ordemServicoResquest){
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setValorTotal(ordemServicoResquest.valorTotal());
        ordemServico.setMaoObra(ordemServicoResquest.maoObra());
        ordemServico.setStatus(StatusOS.Aberta);
        return ordemServico;
    }

    public OrdemServicoResponse tranformarEmResposta(OrdemServico ordemServico){
        return new OrdemServicoResponse(
                ordemServico.getOrdemServicoId(),
                ordemServico.getStatus(),
                ordemServico.getValorTotal(),
                ordemServico.getDataAbertura(),
                ordemServico.getDataFechamento(),
                ordemServico.getMaoObra()
        );
    }
}
