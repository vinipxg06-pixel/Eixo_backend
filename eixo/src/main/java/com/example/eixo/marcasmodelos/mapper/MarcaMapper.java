package com.example.eixo.marcasmodelos.mapper;

import com.example.eixo.marcasmodelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasmodelos.model.Marca;
import org.springframework.stereotype.Component;

@Component
public class MarcaMapper {

    public MarcaResponse toResponse (Marca marca){
        return new MarcaResponse(
                marca.getIdMarca(),
                marca.getNomeMarca()
        );
    }
}
