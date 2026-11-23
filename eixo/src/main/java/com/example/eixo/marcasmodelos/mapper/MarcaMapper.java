package com.example.eixo.marcasmodelos.mapper;

import com.example.eixo.config.MapStructConfig;
import com.example.eixo.marcasmodelos.api.dto.MarcaResponse;
import com.example.eixo.marcasmodelos.model.Marca;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MarcaMapper {

    MarcaResponse paraResposta(Marca marca);
}
