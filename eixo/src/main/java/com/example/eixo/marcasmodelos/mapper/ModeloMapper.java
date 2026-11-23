package com.example.eixo.marcasmodelos.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.marcasmodelos.api.dto.ModeloResponse;
import com.example.eixo.marcasmodelos.model.Modelo;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class, uses = MarcaMapper.class)
public interface ModeloMapper {

    ModeloResponse paraResposta(Modelo modelo);
}
