package com.example.eixo.pecasestoque.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PecaEstoqueMapper {

    // ativa fica com o valor padrão da entidade; id, oficina e datas são definidos pelo PecaEstoqueService
    @Mapping(target = "estoqueId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    @Mapping(target = "listaOcPecas", ignore = true)
    @Mapping(target = "listaOsPecas", ignore = true)
    @Mapping(target = "ativa", ignore = true)
    PecaEstoque paraEntidade(PecaEstoqueRequest pecaEstoqueRequest);

    @Mapping(target = "oficinaId", source = "oficina.oficinaId")
    // regra da entidade, não o campo cru (ativa pode estar null)
    @Mapping(target = "ativa", expression = "java(pecaEstoque.estaAtiva())")
    PecaEstoqueResponse paraResposta(PecaEstoque pecaEstoque);
}
