package com.example.eixo.ocpecas.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.ocpecas.api.dto.OcPecasResponse;
import com.example.eixo.ocpecas.model.OcPecas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OcPecasMapper {

    @Mapping(target = "estoqueId", source = "pecaEstoque.estoqueId")
    @Mapping(target = "nomePeca", source = "pecaEstoque.nomePeca")
    @Mapping(target = "subtotal", expression = "java(item.subtotal())")
    OcPecasResponse paraResposta(OcPecas item);
}
