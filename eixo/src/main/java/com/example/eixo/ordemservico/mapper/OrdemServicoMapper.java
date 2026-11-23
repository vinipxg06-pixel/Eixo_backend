package com.example.eixo.ordemservico.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.model.OrdemServico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OrdemServicoMapper {

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "veiculoId", source = "veiculo.idVeiculo")
    // OS aberta sem orçamento: orcamentoId sai null
    @Mapping(target = "orcamentoId", source = "orcamento.idOrcamento")
    // orçamento aprovado não muda mais, então o total dele é o valor que o cliente aprovou
    @Mapping(target = "valorAprovado", source = "orcamento.valorTotal")
    OrdemServicoResponse paraResposta(OrdemServico ordemServico);
}
