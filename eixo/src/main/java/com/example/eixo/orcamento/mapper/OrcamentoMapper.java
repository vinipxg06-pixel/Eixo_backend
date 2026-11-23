package com.example.eixo.orcamento.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.orcamento.api.dto.OrcamentoResponse;
import com.example.eixo.orcamento.model.Orcamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OrcamentoMapper {

    @Mapping(target = "orcamentoId", source = "idOrcamento")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "veiculoId", source = "veiculo.idVeiculo")
    OrcamentoResponse paraResposta(Orcamento orcamento);
}
