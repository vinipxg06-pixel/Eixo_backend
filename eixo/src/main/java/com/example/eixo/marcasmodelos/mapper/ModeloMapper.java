package com.example.eixo.marcasmodelos.mapper;

import com.example.eixo.marcasmodelos.api.dtos.ModeloResponse;
import com.example.eixo.marcasmodelos.model.Modelo;
import org.springframework.stereotype.Component;

@Component
public class ModeloMapper {

    private final MarcaMapper marcaMapper;

    public ModeloMapper(MarcaMapper marcaMapper) {
        this.marcaMapper = marcaMapper;
    }

    public ModeloResponse toResponse(Modelo modelo) {
        return new ModeloResponse(
                modelo.getIdModelo(),
                modelo.getNomeModelo(),
                marcaMapper.toResponse(modelo.getMarca())
        );
    }
}
