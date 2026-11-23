package com.example.eixo.oficina.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.oficina.api.dto.OficinaRequest;
import com.example.eixo.oficina.api.dto.OficinaResponse;
import com.example.eixo.oficina.model.Oficina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OficinaMapper {

    @Mapping(target = "oficinaId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "listaClientes", ignore = true)
    @Mapping(target = "listaUsuarios", ignore = true)
    @Mapping(target = "listaOrcamentos", ignore = true)
    @Mapping(target = "listaPecasEstoque", ignore = true)
    @Mapping(target = "listaVeiculos", ignore = true)
    Oficina paraEntidade(OficinaRequest oficinaRequest);

    OficinaResponse paraResposta(Oficina oficina);
}
