package com.example.eixo.marcasModelos.mapper;

import com.example.eixo.marcasModelos.api.dtos.ModeloRequest;
import com.example.eixo.marcasModelos.api.dtos.ModeloResponse;
import com.example.eixo.marcasModelos.model.Modelo;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class ModeloMapper {

    public Modelo toEntity(ModeloRequest modeloRequest){
        Modelo modelo = new Modelo();

        modelo.setNomeModelo(modeloRequest.nomeModelo());
        return modelo;
    }

    public ModeloResponse toResponse(Modelo modelo){
        return new ModeloResponse(
                modelo.getIdModelo(),
                modelo.getNomeModelo(),
                modelo.getMarca()
        );
    }
}
