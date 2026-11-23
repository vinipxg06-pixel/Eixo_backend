package com.example.eixo.cliente.mapper;

import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface ClienteMapper {

    // id, oficina, status e datas são definidos pelo ClienteService
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    @Mapping(target = "listaOrcamentos", ignore = true)
    @Mapping(target = "listVeiculos", ignore = true)
    Cliente paraEntidade(ClienteRequest clienteRequest);

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    ClienteResponse paraResposta(Cliente cliente);
}
