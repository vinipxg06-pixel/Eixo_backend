package com.example.eixo.ospecas.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.ospecas.api.dto.OsPecasResponse;
import com.example.eixo.ospecas.model.OsPecas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OsPecasMapper {

    @Mapping(target = "estoqueId", source = "pecaEstoque.estoqueId")
    @Mapping(target = "nomePeca", source = "pecaEstoque.nomePeca")
    @Mapping(target = "valorTotal", expression = "java(osPecas.subtotal())")
    OsPecasResponse paraResposta(OsPecas osPecas);
}
