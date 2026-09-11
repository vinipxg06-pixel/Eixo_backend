package com.example.eixo.marcasmodelos.mapper;

import com.example.eixo.marcasmodelos.api.dtos.MarcaRequest;
import com.example.eixo.marcasmodelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasmodelos.model.Marca;
import org.springframework.stereotype.Component;

@Component
public class MarcaMapper {

    public Marca toEntity(MarcaRequest marcaRequest){
        Marca marca = new Marca();

        marca.setNomeMarca(marcaRequest.nomeMarca());
        return marca;
    }

    public MarcaResponse toResponse (Marca marca){
        return new MarcaResponse(
                marca.getIdMarca(),
                marca.getNomeMarca()
        );
    }
}
