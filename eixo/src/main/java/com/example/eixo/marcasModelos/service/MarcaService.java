package com.example.eixo.marcasModelos.service;

import com.example.eixo.marcasModelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasModelos.mapper.MarcaMapper;
import com.example.eixo.marcasModelos.model.Marca;
import com.example.eixo.marcasModelos.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaService(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    public List<MarcaResponse> findAllMarcas(){
        List<Marca> listaMarcas = marcaRepository.findAll();
        List<MarcaResponse> listaMarcasResponse = new ArrayList<>();

        for(Marca marca : listaMarcas){
            MarcaResponse marcaResponse = marcaMapper.toResponse(marca);
            listaMarcasResponse.add(marcaResponse);
        }
        return listaMarcasResponse;
    }

    public MarcaResponse findMarcaById(Long id) {
        Marca marcaEncontrada = marcaRepository.findById(id).get();
        return marcaMapper.toResponse(marcaEncontrada);
    }
}
