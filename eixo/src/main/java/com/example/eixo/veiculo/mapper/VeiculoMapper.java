package com.example.eixo.veiculo.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.marcasmodelos.mapper.ModeloMapper;
import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.model.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = ModeloMapper.class)
public interface VeiculoMapper {

    // id, oficina, modelo, cliente e datas são definidos pelo VeiculoService
    @Mapping(target = "idVeiculo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    @Mapping(target = "modelo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Veiculo paraEntidade(VeiculoRequest veiculoRequest);

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    @Mapping(target = "nomeCliente", source = "cliente.nomeCliente")
    VeiculoResponse paraResposta(Veiculo veiculo);
}
