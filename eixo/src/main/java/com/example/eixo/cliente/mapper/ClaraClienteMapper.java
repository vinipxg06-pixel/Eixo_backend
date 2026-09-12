package com.example.eixo.cliente.mapper;


import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClaraClienteMapper {

    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    Cliente toEntity(ClienteRequest clienteRequest);

    @Mapping(source = "oficina.oficinaId", target = "oficinaId")
    ClienteResponse toResponse(Cliente cliente);
}

