package com.example.eixo.marcasModelos.mapper;

import com.example.eixo.marcasModelos.api.dtos.MarcaRequest;
import com.example.eixo.marcasModelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasModelos.model.Marca;
import jakarta.persistence.Column;
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
